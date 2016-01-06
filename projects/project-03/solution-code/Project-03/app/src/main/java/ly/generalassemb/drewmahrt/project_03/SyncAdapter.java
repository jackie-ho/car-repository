package ly.generalassemb.drewmahrt.project_03;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.ParentReference;

import org.json.JSONException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ly.generalassemb.drewmahrt.project_03.activities.ItineraryListActivity;

/**
 * Created by Drew on 12/2/15.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = SyncAdapter.class.getSimpleName();
    private ContentResolver mContentResolver;
    private AccountManager mAccountManager;


    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
        mAccountManager = AccountManager.get(context);
    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs){
        super(context,autoInitialize,allowParallelSyncs);
        mContentResolver = context.getContentResolver();
        mAccountManager = AccountManager.get(context);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        try {
            performSync(account, extras, authority, provider, syncResult);
        } catch (final AuthenticatorException e) {
            Log.e(TAG, "Synchronise failed ", e);
            syncResult.stats.numParseExceptions++;
        } catch (final OperationCanceledException e) {
            Log.e(TAG, "Synchronise failed ", e);
        } catch (final IOException e) {
            Log.e(TAG, "Synchronise failed ", e);
            syncResult.stats.numIoExceptions++;
        } catch (final JSONException e) {
            Log.e(TAG, "Synchronise failed ", e);
            syncResult.stats.numParseExceptions++;
        } catch (final RuntimeException e) {
            Log.e(TAG, "Synchronise failed ", e);
            // Treat runtime exception as an I/O error
            syncResult.stats.numIoExceptions++;
        }
    }

    private void performSync(Account account, Bundle extras,
                             String authority, ContentProviderClient provider,
                             SyncResult syncResult) throws AuthenticatorException,
            OperationCanceledException, IOException, ParseException, JSONException {

        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        com.google.api.services.drive.Drive driveService = new com.google.api.services.drive.Drive.Builder(
                transport, jsonFactory, ItineraryListActivity.mCredential)
                .setApplicationName("Travel Itinerary")
                .build();

        File remoteDbFile = findDBInApplicationDataFolder(driveService);

        java.io.File dbpath = this.getContext().getDatabasePath(ItinerarySQLiteOpenHelper.DATABASE_NAME);
        long lastModified = dbpath.lastModified();
        boolean compareValue = remoteDbFile.getModifiedDate().getValue() > lastModified;

        if(compareValue){
            //remote is newer
            InputStream in = downloadFile(driveService,remoteDbFile);
            byte[] buffer = new byte[in.available()];
            in.read(buffer);

            java.io.File targetFile = new java.io.File("/data/data/ly.generalassemb.drewmahrt.project_03/databases/"+ItinerarySQLiteOpenHelper.DATABASE_NAME);
            OutputStream outStream = new FileOutputStream(targetFile);
            outStream.write(buffer);
        } else {
            //local is newer
            insertFileInApplicationDataFolder(driveService,ItinerarySQLiteOpenHelper.DATABASE_NAME,"","",ItinerarySQLiteOpenHelper.DATABASE_NAME,dbpath);
        }

    }

    private static File insertFileInApplicationDataFolder(Drive service, String title, String description,
                                                          String mimeType, String filename, java.io.File newFile) {
        Log.d("DREWTEST","INSERTING FILE INTO DRIVE");
        // File's metadata.
        File body = new File();
        body.setTitle(title);
        body.setDescription(description);
        body.setMimeType(mimeType);
        body.setParents(Arrays.asList(new ParentReference().setId("appfolder")));

        // File's content.
        FileContent mediaContent = new FileContent(mimeType, newFile);
        try {
            File file = service.files().insert(body, mediaContent).execute();
            return file;
        } catch (IOException e) {
            System.out.println("An error occured: " + e);
            return null;
        }
    }

    private static File findDBInApplicationDataFolder(Drive service) {
        Log.d("DREWTEST","Retrieving file list from Drive");
        List<File> result = new ArrayList<File>();
        Drive.Files.List request = null;
        try {
            request = service.files().list();
        } catch (IOException e) {
            e.printStackTrace();
        }
        request.setQ("'appfolder' in parents");

        do {
            try {
                FileList files = request.execute();

                result.addAll(files.getItems());
                request.setPageToken(files.getNextPageToken());
            } catch (IOException e) {
                System.out.println("An error occurred: " + e);
                request.setPageToken(null);
            }
        } while (request.getPageToken() != null &&
                request.getPageToken().length() > 0);

        for(int i=0;i<result.size();i++) {
            if(result.get(i).getTitle().equals(ItinerarySQLiteOpenHelper.DATABASE_NAME))
                return result.get(i);
        }

        return null;
    }

    private static InputStream downloadFile(Drive service, File file) {
        if (file.getDownloadUrl() != null && file.getDownloadUrl().length() > 0) {
            try {
                HttpResponse resp =
                        service.getRequestFactory().buildGetRequest(new GenericUrl(file.getDownloadUrl()))
                                .execute();
                return resp.getContent();
            } catch (IOException e) {
                // An error occurred.
                e.printStackTrace();
                return null;
            }
        } else {
            // The file doesn't have any content stored on Drive.
            return null;
        }
    }
}
