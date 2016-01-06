---
title: Functions (Methods) and Scope
type: lesson
duration: "1:25"
creator:
  name: Kristen Tonga
  city: NYC
competencies: Programming fundamentals in Java
---

<!--  OUTSTANDING

1. Functions relevance in opening
2. Demo of writing a function in the Demo: Writing Functions Section
3. Solution code for independent practice
4. Make the guided practice more straightfoward

-->

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

Functions are essential to write Java and keep the code [DRY](https://en.wikipedia.org/wiki/Don%27t_repeat_yourself).

> TODO Give relevance, background, a funny story

## Introduction: Writing Functions and Languages (15 mins)

**TODO Demo something cool.**

Note the methods.
Note methods are called from the main method.

#### What we did
Before we talk about scope, there's a few things to understand before thinking about what scope actually means.

The code that you write *must* be translated into a form that the computer can understand.

Source code is human readable, *we hope*. This source code may be translated into a set of 1's and 0's that a computer's CPU can understand. Yep, the CPU is a chip on the computer that does all the processing.  There is a reason it's called the Central Processing Unit, or CPU.

`Source Code`  ==>  `1's and 0's`

...or, the source code may be translated into a another type of language, byte code, that can be understood by a Virtual Machine(VM). A Virtual Machine executes a program, by translating each VM readable statement into a sequence of one or more subroutines already compiled into machine code. In the case of Java, the JVM understands bytecode.

`Source Code` ==> `byte code`(understood by JVM and mapped to `1's and 0's` that the computer's CPU can understand)

#### Compiled Languages

Some languages are *explicitly* compiled. In other words, the programmer must run particular commands to invoke compilation.

**Java** is one of these languages. To compile java code, the programmer must run a command like:

`javac MainActivity java MainActivity.class`

The `javac` command translates the Java code in the YourFile.java file into an *executable* or *binary* file (a YourFile.class file) that contains the `bytecode` understood by the JVM. This is what is done under the hood by a IDE like IntelliJ.

The *JVM* is the **Java** compiler.

So here's what happens:

`Source Code`  ==>  `1's and 0's`

`MainActivity.java`  ==>  `MainActivity.class`

#### Interpreted Languages
Some languages do *not* require the programmer to explicitly run a compiler. **JavaScript**, and **Ruby** are a couple of interpreted languages. There is still compilation being done, but it's done automatically.

`Source Code` ==> `byte code`

#### From Source to Running Code.
There are two basic phases to go through when going from code in a file to a program running.

- Compile Time - a phase when the source code is translated to another form. For example, when we run the `javac HelloWorld` command, java program will compile java to an intermediate language/bytecode that the Java Virtual Machine(JVM) understands.

- Runtime - a phase when the computer actually runs each statement in the program.  For example, when we run the `java HelloWorld.class` this is when the computer runs the java program bytecode.


#### Main Method
The main method is where everything starts.
From the Oracle Java Documentation:

In the Java programming language, every application must contain a main method whose signature is:

```java
public static void main(String[] args)
```

> Instructor Note: Consider writing the main method signature on the board, so you can underline and point to things (modifiers, parameters) as you go through the following.

The modifiers public and static can be written in either order (public static or static public), but the convention is to use public static as shown above. You can name the argument anything you want, but most programmers choose "args" or "argv".

The main method is similar to the main function in C and C++; it's the entry point for your application and will subsequently invoke all the other methods required by your program.

The main method accepts a single argument: an array of elements of type String.

```java
public static void main(String[] args)
```

This array is the mechanism through which the runtime system passes information to your application. For example:

```
java MyApp arg1 arg2
```

Each string in the array is called a command-line argument. Command-line arguments let users affect the operation of the application without recompiling it.

>Check: What does an `entry point` mean?


## Demo: Let's break it down (15 mins)

Let's look at what the parts of this method do. Let's start with the basics, which we covered a bit in explaining the main method..

> Instructor Note: Write method on the board (or add body to main method signature, at the appropriate time) and underline and label each part as you go through the following sections.

```java
public                     void            interestingMethod(  String input  )     throws IOException
//<modifiers/visibility>  <return type>    <method name>   (  <parameters>  )     <Exception list>
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

>Instructor Note: Refer back to main method and point out `static` modifier. Perhaps eliminate static from a method and point out the Lint error **

Any method that is called from within a static context must also be static. So, for all methods, for now use, `public static`. Again, we'll explain more what this means later.


#### Return Type

A method can return a value, but the type of that returned data must be specified so that the calling function knows what to do with it.

>Instructor Note: Invite students to code along if they wish. Create a new java program, using the given console template from IntelliJ, or create a basic class with an inner main method. Then code the following two methods.  Use the commented out print statements to explain local scope.

The problem:

```java
class Main {
    int mSum;
    public static void main(String[] args) {
        getSum();
        // System.out.println(sum); // not available
    }
    public static void getSum() {
        int sum = 2 + 2;
        System.out.println(sum);
    }
  }
```

The solution:

```java
class Main {
    int mSum;
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

> Instructor Note: Give an example of when it is appropriate to use a return type, and another example when a global variable is more appropriate.
> Check: What are some data types a method could return? Why might you want to return a value from a method?  What is the naming convention for global variables and where do you put them?


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
public static int returnSum(int num1) {
    int sum = num1 + num1;
    return sum;
}
```

How about two numbers?

```java
public static int returnSum(int num1, int num2) {
    int sum = num1 + num2;
    return sum;
}
```

Now, note, the method can be called like so:
```java
public static void main(String[] args) {
    int returned = returnSum(2,4);
    System.out.println(returned);
    int returned = returnSum(10,52);
    System.out.println(returned);
}
```

>Instructor Note: Emphasize the ability to reuse code without re-writing it. Remind students of the `DRY` principle.

It is also possible to have a return type for an unknown number of arguments, which can be declared like so:
```java
public static void myFunction(String... vars) {}
```

or like so:

```java
public static void myFunction(String[] vars) {}
```

These two signatures the same thing under the covers. From where does it look familiar?

The main class! Like main, myFunction will take an indefinite amount of parameters of the type String. For now, just know it exists.

In java, if a method declares a parameter, that *parameter* is required to be sent as an *argument* from the calling method.


#### Method Body (Enclosed within curly braces)

This is where the main functionality of your method will be called.


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
Please create a new Java project in IntelliJ and work through as many as these exercises as you can within the next 15 mins. Use the official [Oracle Java Docs](https://docs.oracle.com/javase/tutorial/java/javaOO/methods.html) to help you through these exercises and look up the different class methods you can use. 


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

4. Write a method called 'reverseString'. This method should take one argument, a String. The method should return a string with the order of the words reversed. Don't worry about punctuation

    ```java
    reverseString("black cat"); => "tac kcalb"
    reverseString("the cow jumped over the moon"); => "noom eht revo depmuj woc eht"
    reverseString("I can ride my bike with no handlebars"); => "srabeldnah on htiw ekib ym edir nac I"
    ```

## Conclusion (5 min)
- Why do we use methods?
- When might you use a method?

## Resources:
- [Oracle Java Docs - Defining Methods](https://docs.oracle.com/javase/tutorial/java/javaOO/methods.html)
- [Oracle Java Docs - A Closer Look at the "Hello World!" Application](https://docs.oracle.com/javase/tutorial/getStarted/application/)
- [Princeton Java Cheat sheet](http://introcs.cs.princeton.edu/java/11cheatsheet/)
- [Java Modifier Types](http://www.tutorialspoint.com/java/java_modifier_types.htm)
- [Princeton Recursion](http://introcs.cs.princeton.edu/java/23recursion/)
