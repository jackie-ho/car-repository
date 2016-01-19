
---

| Title | Duration | Name | City |
| --- | --- | --- | --- |
| Intro to XML | 1:30 | Yuliya Kaleda | NYC |

---
# ![](https://ga-dash.s3.amazonaws.com/production/assets/logo-9f88ae6c9c3871690e33280fcf557f33.png) Intro to XML

### LEARNING OBJECTIVES
*After this lesson, you will be able to:*
- Identify what XML is and what it is used for
- Describe the basics parts of XML: elements, attributes, XML namespace
- Describe how an element can contain child elements
- Describe how empty elements work

### STUDENT PRE-WORK
*Before this lesson, you should already be able to:*
- Edit files in a text editor

### INSTRUCTOR PREP
*Before this lesson, instructors will need to:*
- Open and test the provided XML code

---

## Opening (5 mins)
To create an Android app you need not only Java, but xml as well. XML is the language that is used to write:

* a Manifest file (the contract where all components of the app are registered), which is located in the folder manifests/
* layout and values files that can be found in the folder res/

Let's go over some simple vocabulary that we'll need to know:

*Parser* - a program, usually part of a compiler, that receives input in the form of sequential source program instructions, interactive online commands, markup tags, or some other defined interface and breaks them up into parts (for example, the nouns (objects), verbs (methods), and their attributes or options) that can then be managed by other programming (for example, other components in a compiler).

*Extensible* - a quality of something, such as a program, programming language, or protocol, that is designed so that users or developers can expand or add to its capabilities.

## Introduction: What is XML? (10 mins)

* XML stands for Extensible Markup Language
* XML is a markup language much like HTML
* XML was designed to store and transport data
* XML was designed to be self-descriptive

#### Syntax

The simplest XML elements contain an opening tag, a closing tag, and some content. The opening tag begins with a left angle bracket (<),
followed by an element name that contains letters and numbers (but no spaces), and finishes with a right angle bracket (>). In XML,
content is usually parsed character data. It could consist of plain text, other XML elements, and more exotic things like XML entities,
comments, and processing instructions. Following the content is the closing tag, which exhibits the same
spelling and capitalization as your opening tag, but with one tiny change: a / appears right before the element name.

##### Example:

```xml
<elements>
    <myelement>one</myelement>
    <myelement>two</myelement>
 </elements>
 ```

#### Elements:

A tag – either opening or closing – is used to mark the start or end of an element. In the above example we have 3 elements: "elements" and 2 "myelement". Pay attention that the element names are case sensitive and in our example mean different things.

#### Attributes:

Inside the tag, following the element name, there can be some data (e.g. type="Main element"). This is called an attribute.
You can think of attributes as adjectives – they provide additional information about the element that may not make any sense as content.
If we modify our example and add attributes to each element, our example will look like this:

```xml
<elements name="parentElement">
    <myelement name="childElement1">one</myelement>
    <myelement name="childElement2">two</myelement>
 </elements>
 ```


## Codealong: Create a simple XML file in Android Studio (10 mins)

Let's create a simple XML layout together. Take a car as an example. We can have attributes for make, model, color, year. We can have child elements that represent the parts in the car, such as wheels, engine, seats.

```xml
<car make="honda" model="civic" color="blue">
  <wheels>4</wheels>
  <engine cylinders="4"></engine>
  <seats>4</seats>
</car>
```


## Independent Practice: Create a layout file (15 mins)  
Now that we've seen the basic syntax for an XML file, let's make one together in Android Studio. We're going to create a "ga-class" parent element with various child elements, such as classroom, start time, end time, instructors, and students.

Parent element:
- "ga-class" parent element
- "name" attribute for ga-class with the value "ADI"
- classroom attribute for ga-class with the value "2B"
Child elements:
- Two instructor elements, one with the content "James", and the other with the content "Drew"
- Student elements, with names as content, and an "age" attribute


## Introduction: Namespacing (15 mins)

 One of the features of xml is that you can define your own elements, which provides flexibility and scope. But it also creates the strong possibility that, when combining XML content from different sources, you’ll experience clashes between code in which the same element names serve very different purposes. For example, if you’re running a bookstore, your use of <element> tags in XML may be used to track books. A mortgage broker would use ```<element>``` in a different way – perhaps to track a deed. A dentist or doctor might use ```<element>``` to refer to chemical solution. Here is where problems can arise. To solve this ambiguity XML namespaces attempt to keep different semantic usages of the same XML elements separate. In our example, each person could define their own namespace and then prepend the name of their namespace to specific tags:  ```<book:element> ``` is different from ```<broker:element>``` and ```<medrec:element>```. A Namespace is a set of unique names. A namespace is declared as follows:

 ```xml
<elements xmlns:pfx="http://www.example.com"></elements>
 ```

In the attribute xmlns:pfx, xmlns is like a reserved word, which is used only to declare a namespace. In other words, xmlns is used for binding namespaces. A namespace has a prefix, in our example it is "pfx", and URI, "http://www.example.com". Although a namespace usually looks like a URL, that doesn't mean that one must be connected to the Internet to actually declare and use namespaces. Our full example would look like this:

 ```xml
<elements xmlns:pfx="http://www.example.com">
 <pfx:myelement>one</pfx:myelement>
 <pfx:myelement>two</pfx:myelement>
</elements>
 ```
The tags ```<myelement>``` are associated with the namespace "http://www.example.com".

#### Nesting

As you might notice from our example. There is a special structure of the xml code and indentation is different. The tag ```<elements>``` closes after nested elements ```<myelement>```. Thus, ```<myelement>``` parts are considered to be children of the parent ```<elements>```.

Some XML elements are said to be empty – they contain no content whatsoever. Remember that in XML all opening tags must be matched by a closing tag. For empty elements, you can use a single empty-element tag to replace this:

 ```xml
<myEmptyElement></myEmptyElement>
 ```

 with this:

```xml
 <myEmptyElement/>
 ```
The / at the end of this tag basically tells the parser that the element starts and ends right here. It’s an efficient shorthand method that you can use to mark up empty elements quickly.


## Independent Practice: Add to your Layout file (15 mins)
Let's make some changes to the ga-class element we created earlier:

- Add the prefix "ga" and the namespace "http://ga-class.com" to the ga-class element
- Use the "ga" prefix on child elements
- Change the classroom element to be an empty child element with the attributes name("3B") and seats("35")
- Nest each student element to be within a new "students" element
- Add a classroom-supplies child element, which contains marker elements with the content "blue", "green", and "red" .


## Demo: Basic XML example in Android (10 mins)

Now that we have covered all the basics of XML, let's take a look at how it gets integrated into an Android app. Import the project "IntroIntoXml". Open the AndroidManifest.xml file:

- the parent element is "manifest" with a namespace "http://schemas.android.com/apk/res/android";
- the "manifest" tag has a child "application", which also has just one child element "activity";
- the "application" element has 5 attributes and their values: allowBackup, icon, label, supportsRtl, theme. It includes a child element "intent-filter", which has 2 empty elements "name".

Now, open the activity_main.xml

- the parent element is "RelativeLayout" with a lot of attributes: layout_width, layout_height, paddingLeft, paddingRight, etc. It has the namespace "http://schemas.android.com/apk/res/android";
- it has 2 children: TextView and Spinner;
- TextView and Spinner are considered to be siblings elements;  
- TextView has the following attributes: id, text, layout_width, layout_height, layout_marginTop, textSize, layout_centerHorizontal;
- Spinner includes id, layout_width, layout_height, layout_below, layout_marginTop attributes.


## Independent Practice: Look at an XML file (10 mins)

With a partner, look at the following code/file and answer the questions below:  

```xml
 <?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="generalassembly.yuliyakaleda.supportdifferentdevices">

  <application
      android:allowBackup="true"
      android:icon="@mipmap/ic_launcher"
      android:label="@string/app_name"
      android:supportsRtl="true"
      android:theme="@style/AppTheme">
    <activity android:name=".MainActivity">
      <intent-filter>
        <action android:name="android.intent.action.MAIN" />

        <category android:name="android.intent.category.LAUNCHER" />
      </intent-filter>
    </activity>
    <activity android:name=".InformationActivity" />

    <service
        android:name=".QuickService"
        android:enabled="true">
    </service>
  </application>

</manifest>
```

 1. How many elements does this xml file have?
 2. What is a namespace in this example?
 3. Name parent and children elements.
 4. Find and name empty elements (the ones that do not have content).

 > Check: Were the students able to complete the questions above?

## Conclusion (5 mins)

As we can see from our example app, it is of vital importance to be proficient in using xml to create an Android Manifest and resource (layout, values) files. Without a manifest, which is the main file where all the Android components get registered, and res/ folder, which stores all the UI files, our app cannot be functional.
