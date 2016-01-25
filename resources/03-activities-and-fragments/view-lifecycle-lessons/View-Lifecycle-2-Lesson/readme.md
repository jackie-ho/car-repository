---
title: View Life Cycle (part 2)
duration: "1:30"
creator:
    name: Drew Mahrt
    city: NYC
standard: Standard Title
---

# ![](https://ga-dash.s3.amazonaws.com/production/assets/logo-9f88ae6c9c3871690e33280fcf557f33.png) Activity Life Cycle (part 2)
Week 3 | Lesson 4

### LEARNING OBJECTIVES
*After this lesson, you will be able to:*
- Implement Shared Preferences for saving persistent data
- Understand some reasons to use onPause and onResume
- Implement freeing system resources using onPause and onResume

### STUDENT PRE-WORK
*Before this lesson, you should already be able to:*
- Understand the Activity Life Cycle

### INSTRUCTOR PREP
*Before this lesson, instructors will need to:*
- Gather materials needed for class
- Complete Prep work required
- Prepare any specific instructions

---
<a name="opening"></a>
## Opening (5 mins)

As we discussed in the last lesson, there are a lot of important steps in the Activity Life Cycle. We are now going to concentrate specifically on onPause and onResume, exploring why they are useful and examples of when to use them. Also in the last lesson, we talked about saving state using bundles, First we're going to work on saving persistent data using the Shared Preferences, then we're going to integrate that into onPause and onResume.


***

<a name="introduction"></a>
## Introduction: Persistent Data with SharedPreferences (15 mins)

Sometimes we need the ability to store and retrieve data even when the app is completely closed. Previously, we were storing data in Bundles, which only lasted while the app was running. There are many ways to persist data, such as files and databases. Android provides a built-in utility for this called SharedPreferences. SharedPreferences allows you to store any type of data using key-value pairs, where the key is a String, and the value is the data. These preferences remain with the app as long as it is installed.


One popular use for SharedPreferences are to hold app settings. For instance, a news aggregator app remembers the type of news articles and the preferred sources you like to receive your news from. You don't want to set this every time, so the app stores this information in SharedPreferences for easy retrieval the next time the app launches.

***

<a name="demo"></a>
## Demo: Persistent Data with SharedPreferences (10 mins)


To start with, we must retrieve the SharedPreferences Object from Android for your app.

There are two ways to do this. The first is used if you only need to use these SharedPreferences in this one activity.

```java
SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
```

The second way allows you to provide a key which matches a specific SharedPreferences file. This key must be unique to your app, such as "com.example.myapp.SOME_KEY_NAME". These preferences, unlike the first, can be retrieved from anywhere in your app.

```java
SharedPreferences sharedPref = context.getSharedPreferences("com.example.myapp.SOME_KEY_NAME",Context.MODE_PRIVATE);
```

With that done, we are now able to read from SharedPreferences. To write to SharedPreferences, we must retrieve an object called an Editor. Let's look at an example for both of these.

```java
//To read String
String str = sharedPreferences.getString("exampleKey","DEFAULT"); //Initial value of the String is "Hello"

//To save String
Editor editor = sharedPreferences.edit();
editor.putString("exampleKey",str+" World!");
editor.commit();
```
After committing the changes, the String with key "exampleKey" now contains the value "Hello World!" instead of "Hello".


***

<a name="guided-practice"></a>
## Guided Practice: Persistent Data with SharedPreferences (10 mins)

In this practice, we will adapt the app from the previous lesson that saved the time the app was launched to use SharedPreferences instead of a Bundle.


***

<a name="independent-practice"></a>
## Independent Practice: Persistent Data with SharedPreferences (15 mins)

Now it's time to save SharedPreferences data in one activity, and retrieve it in another. We will Have a EditText with a button in the first activity. When the button is pressed, we will be taken to the next activity, and whatever was typed into the text box will be shown in the second activity. Starter code is provided in the starter-code directory.


***

<a name="introduction"></a>
## Introduction: onPause and onResume (15 mins)

As we discussed in the last lesson, there are a number of different methods that are automatically called during the activity creation, pausing, resuming, and destruction processes. Two of those methods are onPause and onResume. onPause is called when user interaction is being taken away, but the activity is still currently visible (although this could change immediately afterwards). onResume is called when the activity is visible again and control is being given back to the user. Because these two methods are guaranteed to be called before and after resuming and pausing the activity, respectively, they are the perfect time to perform basic maintenance.

One of the most common uses of these methods are to gain and release system resources. Generally, these are things that can be battery draining (ie long running background processes that are only needed while visible, like a streaming video) or system resources that must be acquired (ie camera). When resuming, you gain the resource, and when pausing, you release the resource.


***

<a name="demo"></a>
## Demo / Codealong: onPause and onResume (5 mins)

In this demo, we will be looking at when onPause and onResume are called in a situation where the screen is only partially covered, but the activity is still viewable in the background.

To do this, we will be using a Dialog, which brings a popup over the screen, causing the underlying activity to pause and resume.


***

<a name="guided-practice"></a>
## Guided Practice: Countdown Timer (10 mins)

We are going to be making a basic countdown timer app that is only timing while the initial activity is running. When we switch to another activity, we will pause the time until we return to the first activity.


***

<a name="ind-practice"></a>
## Independent Practice: Music Player (15 minutes)


You will be modifying the provided music player to pause the music when a new activity is opened, and automatically resume it when the user returns to the activity. Starter code is provided in the MusicPlayer project.

**The music used in this app is provided by www.bensound.com under the creative commons license**


***

<a name="conclusion"></a>
## Conclusion (5 mins)

Today we covered SharedPreferences, and how onPause/onResume work in concert to make your app run smoothly. Understanding how the activity lifecycle works, and how to use it to your advantage will make your apps have a much smoother user experience.

***

### ADDITIONAL RESOURCES
- [Shared Preferences](http://developer.android.com/reference/android/content/SharedPreferences.html)
