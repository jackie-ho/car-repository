---
title: Subclassing
duration: "1:30"
creator:
    name: Drew Mahrt
    city: NYC

---

<!--  OUTSTANDING
1. high level conclusion discussion questions 

-->

# ![](https://ga-dash.s3.amazonaws.com/production/assets/logo-9f88ae6c9c3871690e33280fcf557f33.png) Lesson Title


### LEARNING OBJECTIVES
*After this lesson, you will be able to:*
- Describe what subclassing means
- Explain how subclassing works in Java
- Extend a class using Java

### STUDENT PRE-WORK
*Before this lesson, you should already be able to:*
- Create a basic class with getters and setters
- Instantiate a user-defined class

### INSTRUCTOR PREP
*Before this lesson, instructors will need to:*
- Gather materials needed for class
- Complete Prep work required
- Prepare any specific instructions

---
<a name="opening"></a>
## Opening (5 mins)

Today we will be expanding upon our last lesson about classes. We learned about the basic components of a class and how they are created, but they can do so much more. We will be learning about how classes interact with each other, and more specifically, how they are related to each other.


> Check: Ask students to define what a class is, and how to instantiate one.

***

<a name="introduction"></a>
## Introduction: Subclassing (20 mins)

One of the key ideas behind Object-Oriented Programming is defining relationships between the classes you create. In OOP, we create templates that can be reproduced and interacted with. A subclass can be thought of as a more detailed version of a class you have already created.

For instance, a rectangle and a triangle can both be considered a subclass of a shape. We can say that a triangle `is a` shape, and that a rectangle `is a` shape. We could simply make the properties and methods inside of Shape to store the correct amount of sides and calculate the area correctly based on the amount of sides, but our code becomes much clearer if we make a separate class to represent each.

> Check: Ask the students to come up with another example of a superclass and a subclass.

<a name="demo"></a>
## Demo: Topic (15 mins)

Do this with me!

We're going to start with the example of shapes. First, let's define our Shape class with the property "mColor":

``` java
class Shape {
  String mColor;

  public Shape(String color){
    mColor = color;
  }

  //getters and setters
}
```

When designing classes and subclasses, you need to ask yourself what properties or methods are unique to the subclass and what are common across all possible subclasses. In the case of our example, every type of shape has a color, so we include it in the superclass.

Now we're going to make a subclass, Square. A Square is a Shape, so we can make it a subclass:

``` java
class Square extends Shape {
  int mSideLength;

  public Square(int length, String color){
    super(color);
    mSideLength = length;
  }
}
```

There are some important keywords to notice:

- The `extends` keyword denotes that we are subclassing __Car__ for this class. (Making __Car__ our superclass)
- The `super` keyword is used to access members from the superclass, such as the constructor

What we have covered so far are all of the basics you need to build a class and create a subclass using it.

> Check:  Ask the students to come up with another property for Shape, and how we would modify the Shape constructor and super call to accommodate that.  Example: dimensions (2D, 3D)


<a name="guided-practice"></a>
## Guided Practice: Subclassing (30 mins)

This is a tricky topic, so let's get some more guided practice.  Follow along:
Let's write a __Car__ class with the  `mModel` and `mSpeed` member variable which are assigned on instantiation of the __Car__.  How might we do that?

``` java
class Car {
    double mSpeed = 0.0;
    String mModel;
    public Car(String named) {
        mModel = named;
    }
}

// creating a new object of type Car
Car mFirstCar = new Car("DeLorean");
```

That is a good start, but we can create a subclass of this class. So assigning `"DeLorean"` to the mModel variable every time we want a `new` __Car__ is repetitive.

So instead lets create a `class` __DeLorean__ with all the properties we want:

```java
class DeLorean {
    double mSpeed = 0.0;
    private String mModel;
    private String mOwner;

    public DeLorean() {
        mModel = "DeLorean";
    }

     public void goForward() {
        mSpeed = 50.0d;
     }

    public void setOwner(String name) {
        mOwner = name;
    }
}
```

This is better but now we have repeated code across two different file. Java provides us with an important concept in OOP. Inheritance.

We can subclass the __Car__ and our __DeLorean__ class will **inherit** all the methods and variables contained within:

```java
class Car {
    double mSpeed = 0.0;
    protected String mModel;
    protected String mOwner;

    public Car(String name) {
        mModel = name;
    }
//...
}

class DeLorean extends Car {

    public DeLorean() {
        super("DeLorean");
    }

    @Override
    public void goForward() {
        mSpeed = 88.0d;
    }
}
```

We call the constructor of __Car__ with our model name using the method call `super()`. This way now every `new` DeLorean object will have its model name set correctly automatically.

In practice, we can now use DeLorean everywhere we use Car but not the other way around. Think of it in the way a square is rectangle but a rectangle is not a square.

```java
Car exampleCar = new DeLorean();
```

We could also just define it as a DeLorean.

```java
DeLorean exampleDelorean = new DeLorean();
```

> Check: Where have we seen this before in an Android app?

We have seen this before in our previous Android apps when we use the findViewById method to find the instance of our View from the layout.

```java
TextView textView = (TextView)findViewById(R.id.textview);
```
`findViewById` returns a View, but TextView is a subclass of View, so we can `cast` the View to a TextView.


<a name="ind-practice"></a>
## Independent Practice: Topic (15 minutes)

> Instructor Note: This can be a pair programming activity.

Now, you are going to try implementing your own class and subclasses. In pure Java:

- create a superclass Card and subclasses DebitCard and CreditCard
- give your superclass the properties "nameOnCard" and "cardBrand" (Visa, Mastercard, etc.)
- CreditCard should contain the property "cardLimit", and DebitCard should have the property "accountBalance"

Feel free to add any other properties you want~

We have provided the three Java files, but you must add the `extends` keyword in the correct places.

> Check: Were students able to create the desired deliverable(s)? Did it meet all necessary requirements / constraints?



<a name="conclusion"></a>
## Conclusion (5 mins)

Today we gained a further understanding of how multiple classes can fit together to make a useful system that reduces code duplication and makes your code much easier to understand. You can have many levels of subclasses beyond the simple two-level examples we saw today. Hopefully the concepts you learned today helps you understand some of the code that Android Studio is automatically generating for you when you create new Activities. In our next lesson, we will be discussing more concepts and keywords that can be applied to your classes and subclasses.



### ADDITIONAL RESOURCES
- [Inheritance](https://docs.oracle.com/javase/tutorial/java/IandI/subclasses.html)
