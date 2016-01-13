---
title: Functions (Methods) and Scope
type: lesson
duration: "1:25"
creator:
  name: Kristen Tonga
  city: NYC
competencies: Programming fundamentals in Java
---

# Functions (or Methods) and Scope

### Objectives
*After this lesson, students will be able to:*
- Describe how parameters relate to functions
- Compare different types of variable scope
- Identify the parts of a method
- Use naming conventions for methods and variables
- Create and call a function that accepts parameters to solve a problem

### Preparation
*Before this lesson, students should already be able to:*
- Work with basic primitive data types and strings, and assign variables
- Compile and run Java code from the command line

## Opening (5 mins)

Today we're going to be talking about two important topics: methods and scope. Methods are simply blocks of code that can be reused to repeat a specific purpose. Scope is related to methods, and deals with where we are allowed to access the variables we have defined.

> Instructor note: Make sure everyone still has IntelliJ installed

> Check: Ask the students to name a few data types and examples of their use.

## Introduction: Writing Methods (10 mins)

Sometimes we want to re-use code we've written to perform the same tasks multiple times. What are our choices? We could copy/paste the same blocks of code multiple times, but this could get old if we needed to run the same code 20 times. Another option we have are to write methods. We take this re-usable code, and section it off into something called a method.

Methods are essential to write Java and keep the code [DRY](https://en.wikipedia.org/wiki/Don%27t_repeat_yourself).

> Check: Ask the students for examples of methods

## Demo: Let's break it down (25 mins)

Let's look at what the parts of this method do. Let's start with the basics

> Instructor Note: Write method on the board (or add body to main method signature, at the appropriate time) and underline and label each part as you go through the following sections.

```java
public                     void            interestingMethod(  String input  )    
//<modifiers/visibility>  <return type>    <method name>   (  <parameters>  )     
{
<opening brace>
    System.out.println("I am making" + input + "interesting!")
    <method body>
}
<closing brace>

```

#### Modifiers

Modifiers are used to modify how a method can be called.

Access Modifiers include:
- private *(visible only to the class)*
- protected *(visible to the package and all subclasses)*
- public *(visible to the world)*
- friendly *(when none is specified, this is the default)*

Non-Access Modifiers include:
- static *(for creating class methods and variables)*
- final *(for making something permanent)*
- abstract *(to create abstract classes and methods)*
- synchronized / volatile *(used for threads)*


We'll explain more of what all these keywords mean in later lessons.  For now, use `public`!


#### Return Type

A method can return a value, but the type of that returned data must be specified so that the calling function knows what to do with it.

>Instructor Note: Invite students to code along if they wish. Create a new java program, using the given console template from IntelliJ, or create a basic class with an inner main method. Then code the following two methods.  Use the commented out print statements to explain local scope.



The problem:

```java
class Main {
    public static void main(String[] args) {
        getSum();
    }
    public static void getSum() {
        int sum = 2 + 2;
        System.out.println(sum);
    }
  }
```

Great! What if we want to access the sum variable from main? Can we just call it?

This is called `scope`. Variables defined in one method are out of scope of another method. One way to get around this is to return the value from the function so it can be used in another place.

The solution:

```java
class Main {
    public static void main(String[] args) {
        int returned = returnSum();
        System.out.println(returned);
    }
    // public static void getSum() {
        // int sum = 2 + 2;
        // System.out.println(sum);
    // }
    public static int returnSum() {
        int sum = 2 + 2;
        System.out.println(sum);
        return sum;
    }
  }
```

A function executes until it reaches a `return` statement or a closing curly brace.  If a data type has been specified, that sort of data (or null) must be returned or the code will not compile.

Another solution, if it wasn't appropriate to use a return type, would be to use a global variable.

Global variables are defined at the top of a class, and by convention are named using `mVariableName`.

```java
class Main {
    int mSum;
    public static void main(String[] args) {
        getSum();
        System.out.println(mSum);
    }
    public static void getSum() {
        int sum = 2 + 2;
        System.out.println(sum);
        System.out.println(mSum);
        mSum = sum;
    }
  }
```

By placing the variable outside of the method, we are giving all methods below it access. The only way for two methods to access the same variable is to give it `global scope` by making it a member variable.

> Check: Can we make variables of the same name in two different methods? Why?


#### Method Name
This is what the method is called.

It's important to be explicit in the naming of your method so that just by looking at the title - a new developer can come in and can understand what the method will do.

By convention, a method name should be a **verb** in *camel case* that starts in *lowercase*.

Ex: `getName()`, not `GetName()`, nor `getname()`, nor `get_name()`.


#### Parameters (Enclosed within parenthesis)
Parameters are arguments passed into the function when it is called. This makes a function much more dynamic.

Let's take a look back at the sum method.
What would you need to do if you wanted to pass in a number to this method?

```java
private int returnSum(int num1) {
    int sum = num1 + num1;
    return sum;
}
```

How about two numbers?

```java
private int returnSum(int num1, int num2) {
    int sum = num1 + num2;
    return sum;
}
```

Now, note, the method can be called like so:
```java
    int returned = returnSum(2,4);
    System.out.println(returned);
    int returned = returnSum(10,52);
    System.out.println(returned);
```

>Instructor Note: Emphasize the ability to reuse code without re-writing it. Remind students of the `DRY` principle.

It is also possible to have a return type for an unknown number of arguments, which can be declared like so:
```java
private void myFunction(String... vars) {}
```

or like so:

```java
private void myFunction(String[] vars) {}
```

These two signatures the same thing under the covers. From where does it look familiar?

The main class! Like main, myFunction will take an indefinite amount of parameters of the type String. For now, just know it exists.

In java, if a method declares a parameter, that *parameter* is required to be sent as an *argument* from the calling method.


#### Method Body (Enclosed within curly braces)

This is where the main functionality of your method will be called.


## Independent Practice - Other math operators (5 min)

Write methods to perform division, multiplication, and subtraction. Follow the same pattern as addition, where you take in the inputs, and return the result. Print out tests for each method.

> Check: Were all the students able to complete the activity?


## Guided Practice (15 min) - Code along

>Instructor Note: Take a look at the [solution-code](solution-code) and run the askAQuestion java program, so students have an idea what they will be creating.

Let's work through the following example. The Scanner class we'll be creating will be required in the lab.

Some things to mention:

- methods must be within a class definition
- no nesting method in method
- a bit about scope, and mGlobal variables

So here we have a basic main class, that's the first question... What's the next step?

```java
public class Main {
    public static void main(String[] args) {
	    System.out.println("\nAsk: who, what, why, when, or where");
    }
}
```

Let's add a method that creates a gets user input and responds.
```java
public static void askAQuestion() {
    Scanner input = new Scanner(System.in);
    String userInput = input.nextLine();
    if (userString.equals("who")) {
        System.out.println("We're the ADI class.");
    }
}
```

Actually, let's allow the user to put in a more complicated question, such as 'Who are you?'
```java
...
if (userString.contains("who")) {
  ...
}
```

Let's add a default:

```java
  else {
     System.out.println("I don't know how to answer that question.");
     System.out.println("Try again...");
  }
```

Wait, what if we actually want to be able to try again?

```java
    ...System.out.println("Try again...");
    }
    askAQuestion();
}
```


Note, this is called **recursion** - a Recursive method calls itself. For those who are interested in Math, a resource is included that talks about some of the other ways to use recursion to solve basic algorithms.

>Instructor Note: Run the program and prove that it runs circularly.


What if we want to exit out of the program?

```java

    else if (userString.contains("exit")) {
            askAgain();
        }
    }
    public static void askAgain() {
        System.out.println("\nAre you sure you have no more questions? y or n");
        String userInput = grabUserInput();
        if (userInput.equals("y")) {
            System.out.println("Thanks for playing. Goodbye.");
            System.exit(0);
        }
        else if (userInput.equals("n")) {
            System.out.println("Ask another then:");
            askAQuestion();
        }
    }
}
```

## Independent Practice: Write a few functions (15 min)

Please create a new IntelliJ project and work through as many as these exercises as you can within the next 15 mins. Use the official [Oracle Java Docs](https://docs.oracle.com/javase/tutorial/java/javaOO/methods.html) to help you through these exercises and look up the different class methods you can use.

1. Write a method called `divide152By`. This method should accept one argument, a number, and should divide 152 by the given number. For example, the divide152By result of 1 is `152/1` is 152. Your function should return the result.

    Use your function to find the following:

    ```java
    divide152By(3);
    divide152By(43);
    divide152By(8);
    ```

2. Write a method called `thirdAndFirst`. This method accepts two strings, and checks if the third letter of the first string is the same as the first letter of the second string. It should be case insensitive, meaning `thirdAndFirst("Apple","Peon")` should return true.

    Check the following:
    ```java
    thirdAndFirst("billygoat","LION");
    thirdAndFirst("drEAMcaTCher","statue");
    thirdAndFirst("Times","thyme");
    ```

3. Write a method called `transmogrifier`. This method should accept three arguments, which you can assume will be numbers. Your function should return the "transmogrified" result.

    The transmogrified result of three numbers is the product of the first two numbers, raised to the power of the third number.

    For example, the transmogrified result of 5, 3, and 2 is `(5 times 3) to the power of 2` is 225. Use your function to find the following answers.

    ```java
    transmogrifier(5, 4, 3);
    transmogrifier(13, 12, 5);
    transmogrifier(42, 13, 7);
    ```

4. BONUS: Write a method called 'reverseString'. This method should take one argument, a String. The method should return a string with the order of the words reversed. Don't worry about punctuation

Hint: https://docs.oracle.com/javase/7/docs/api/java/lang/StringBuilder.html

    ```java
    reverseString("black cat"); => "tac kcalb"
    reverseString("the cow jumped over the moon"); => "noom eht revo depmuj woc eht"
    reverseString("I can ride my bike with no handlebars"); => "srabeldnah on htiw ekib ym edir nac I"
    ```

## Conclusion (5 min)

Let's review:

- Why do we use methods?
- When might you use a method?
- Describe scope

## Resources:
- [Oracle Java Docs - Defining Methods](https://docs.oracle.com/javase/tutorial/java/javaOO/methods.html)
- [Oracle Java Docs - A Closer Look at the "Hello World!" Application](https://docs.oracle.com/javase/tutorial/getStarted/application/)
- [Princeton Java Cheat sheet](http://introcs.cs.princeton.edu/java/11cheatsheet/)
- [Java Modifier Types](http://www.tutorialspoint.com/java/java_modifier_types.htm)
- [Princeton Recursion](http://introcs.cs.princeton.edu/java/23recursion/)
