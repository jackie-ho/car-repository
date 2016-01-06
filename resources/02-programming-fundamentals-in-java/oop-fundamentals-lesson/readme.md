---
title: Object Oriented Programming Fundamentals
duration: "1:30"
creator:
    name: Drew Mahrt
    city: NYC

---

<!--  OUTSTANDING
1. Ask students to only create one subclass in the independent practice
2. Real world examples of why you would want to limit access to functions and variables in your code. 
-->

# ![](https://ga-dash.s3.amazonaws.com/production/assets/logo-9f88ae6c9c3871690e33280fcf557f33.png) Lesson Title

### LEARNING OBJECTIVES
*After this lesson, you will be able to:*
- Explain the difference between public and private keywords
- Describe how the static keyword relates to variables and methods
- Override methods

### STUDENT PRE-WORK
*Before this lesson, you should already be able to:*
- Create classes and subclasses

### INSTRUCTOR PREP
*Before this lesson, instructors will need to:*
- Gather materials needed for class
- Complete Prep work required
- Prepare any specific instructions

---
<a name="opening"></a>
## Opening (5 mins)

Now it's time to venture beyond the basic class and subclass designs we've learned the last few lessons. Object-Oriented Programming can be truly powerful in the amazing design possibilities it allows programmers to implement in their apps.

Today we will cover topics including private and public member variables, overriding methods, and static and non-static variables and methods. All of these are key components to Android programs you will write. In fact, you have been using almost all of these already without knowing it!

> Check: Ask students to explain the reasons why we use inheritance and subclassing.

<a name="introduction"></a>
## Introduction: Public and Private member variables (15 mins)

We use objects to divide code into separate responsibilities. We can then use these objects in other code to execute their responsibility. How the object accomplishes its responsibility is irrelevant to the calling code - this is known as a `black box`. We only have to worry about the input and outputs of the object and let the object worry about it self.

The big advantage with OOP is creating sectioned off data and code. For black boxes that have implementation only its creator should worry about, it's important to know how to close off access to parts of your `class` while opening access to others.

This is called *access control*, and there are keywords in Java that provide different levels of access.

---
|             	| class 	| package 	| subclass 	| world 	|
|-------------	|-------	|---------	|----------	|-------	|
| `public`     	| Y     	| Y       	| Y        	| Y     	|
| `protected`  	| Y     	| Y       	| Y        	| N     	|
| _no modifier_ 	| Y     	| Y       	| N        	| N     	|
| `private`     	| Y     	| N       	| N        	| N     	|
---

The two most commonly used modifiers are `public` and `private`. These are two modifiers that Java also shares with many other object oriented languages.

#### public and private

By denoting a method or variable `public`, we are exposing it to the world.  This means something different for the variables and methods we create in our class.

- For variables, this means code outside of our class will be able to assign to and from our _member variable_.

- For methods, this means being able to call this method outside of class. In order to this, an object must be instantiated and then, using _dot notation_, the method is called. We have seen this since the very first app that you wrote in this course.

``` java
textView.setText("Hello world!");
```

`setText` is a `public` method of the TextView class. Many of the objects provided by Android have their own `private` methods that you never see in the autocomplete.

>Instructor Note: Explain dot notation

Denoting a member `private` on the other hand will close all access except within the class

> Check: Describe the difference between `public` and `private`.

<a name="demo"></a>
## Demo: Public and Private member variables (5 mins)


Let's go back to our __Car__ example:

```java
class Car {
     private double mSpeed = 0.0d;
     void goForward() {
        mSpeed = 50.0d;
     }

     public void setSpeed(double speed){
       mSpeed = speed;
     }

     public double getSpeed(){
       return mSpeed;
     }
 }

 /// ... in another code file but same package

Car exampleCar = new Car("DeLorean");
exampleCar.goForward();
exampleCar.getSpeed();
```

Notice the private modifier that goes in front of the mSpeed variable. Any calls to a Car object from other code won't have direct access to the variable. That is why getters and setters exist. With a few exceptions, you will almost always make your member variables private.

> Check: Ask why we use getters and setters instead of directly accessing member variables.

<a name="guided-practice"></a>
## Guided Practice: Public and Private member variables (10 mins)

Take 30 seconds to read over the code below.  Tell me where I should be putting `private` and `public`:

``` java
class Dog{
  String mName;
  String mBreed;
  int mAge;

  Dog(String name, String breed, int age){
    mName = name;
    mBreed = breed;
    mAge = age;
  }

  String getName(){...}

  String getBreed(){...}

  int getAge(){...}

  void setName(String name){...}

  void setBreed(String breed){...}

  void setAge(int age){...}

  static void main(String args[]){...}
}
```

## Introduction: Overriding methods (5 mins)

Sometimes we want our subclasses to provide different functionality than their parent classes. Instead of creating a new method to accomplish this, we can Override methods. Basically this just means that the overriding method replaces the parent's version of the method.

**Note**: The new method must be identical to the parent method in declaration.

``` java
public class A{
  public A(){}

  public String hello(int i){
    return "Hello from A "+i;
  }
}

public class B extends A{
  public B(){
    super();
  }

  @Override
  public String hello(int x){
    return "Hello from B "+x;
  }
}
```

Note the @Annotation. This is a concept in Java used to mark certain parts of code for the compiler. The @Override annotation is used to denote superclass methods that we are rewriting. The compiler can then warn us in case of mistake in our override.

> Check: Ask the students what would happen if we used @Override on a method, but declared the method with different input values than the parent method.

<a name="demo"></a>
## Demo: Overriding methods (5 mins)

Let's take another look at our Car class:

Broken code:
``` java
public class Car {
  private double mSpeed;
  ...
  public void increaseSpeed(){
    mSpeed = mSpeed + 5;
  }
}

public class DeLorean extends Car {
    private double mSpeed;
    ...
    @Override
    public int increaseSpeed() {
        mSpeed = mSpeed + 50;
        return mSpeed;
    }
}
```

What is wrong with this code?

Let's fix it:

``` java
public class DeLorean extends Car {
    private double mSpeed;
    ...
    @Override
    public void increaseSpeed() {
        mSpeed = mSpeed + 50;
    }
}
```
We needed to make the increaseSpeed method from DeLorean match the one from Car.

> Check: Were the students able to spot the problem and fix it?


<a name="guided-practice"></a>
## Independent Practice: Overriding methods (10 mins)

Using the starter code in `DogOverride.java`, create a subclass `Poodle.java`. Add a bark method to both, where Poodle's overrides Dog's, so that it returns a different String depending on which type calls it.

> Check:  Were students able to successfully complete the task?  Review one students solution with the class.


## Introduction: Static vs Non-Static (5 mins)

In Java, you will occasionally see a modifier on variables and methods called `static`. This means two different things depending on if it's used on a method or variable.

#### Static methods

A static method makes that method accessible without instantiating an object first. We have seen an example of this when making Toasts.

``` java
Toast.makeText(this,"Hello",Toast.LENGTH_LONG).show();
```

`makeText` is a static method within Toast.

#### Static variables

Static variables are variables that are only created once in memory across all instances of a class. We will see an example of this in the next demo.

> Check: Ask the students to give an example of a static variable we've used.


<a name="demo"></a>
## Demo: Static vs Non-Static (10 mins)

One example of using both static methods and variables is in a design pattern called a Singleton. A singleton is a class whose data is only loaded once into memory, but is accessible anywhere an instance of the class is loaded.

Let's pretend a town only has one School, a class we will make that contains teachers and students. We only want one instance of the school.

``` java
public class School{
  private static School school = null;
  private static LinkedList<String> teachers;
  private static LinkedList<String> students;

  private School(){
    teachers = new LinkedList<String>();
    students = new LinkedList<String>();
  }

  public static School getInstance(){
    if(school == null){
      school = new School();
    }
    return school;
  }

  public void addTeacher(String teacher){
    teachers.add(teacher);
  }

  public void addStudent(String student){
    students.add(student);
  }
}
```
> Check: Ask the students what could happen if we made addTeacher or addStudent static.

## Guided Practice: Static vs Non-Static (5 mins)

Have the students add a static member variable to the `StaticTest.java`, and print it to the console by accessing it as a static property.

> Check: Were students able to successfully complete the task?

<a name="ind-practice"></a>
## Independent Practice: Object Oriented Programming Fundamentals (10 minutes)

Using the starting point of a Plane class in Plane.java, create at least two subclasses, create at least one static variable in one of the classes, and override the startEngine method in plane. You can pretend that methods such as turnPropellor() and igniteJetEngine() already exist. Make sure public and private keywords are included in the required places. Add any additional details you want if you have time.

> Check: Were students able to create the desired deliverable(s)? Did it meet all necessary requirements / constraints?


<a name="conclusion"></a>
## Conclusion (5 mins)

We covered a lot of new concepts today, but they all contribute to making your classes more robust and useful. As you continue writing your Android apps, you will see all of these being used, and hopefully you will now have a better understanding of what they actually are doing.

In addition, today's lesson should help you to be able to use the Android documentation with more ease, since you will know all of the key words surrounding the methods they list.



### ADDITIONAL RESOURCES
- [Oracle: Static documentation](https://docs.oracle.com/javase/tutorial/java/javaOO/classvars.html)
- [Oracle: Controlling Access](https://docs.oracle.com/javase/tutorial/java/javaOO/accesscontrol.html)
- [Oracle: Overriding](https://docs.oracle.com/javase/tutorial/java/IandI/override.html)
