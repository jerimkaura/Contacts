### Contacts
Contacts application to create, update read and delete contact using Room Database

#### Project characteristics

This project brings to table set of best practices, tools, and solutions:

* 100% Kotlin
* Model-View-View Model Architecture
* Dependency Injection
* Material design
* Room local persistence

#### Tech-stack

This project takes advantage of many popular libraries and tools of the Android ecosystem. Most of
the libraries are in the stable version.

* Tech-stack
    * [Kotlin](https://kotlinlang.org/)
        + [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - To perform
          background operations
    * [Dagger Hilt](https://dagger.dev/hilt/) - For dependency injection
    * [Jetpack](https://developer.android.com/jetpack)
        * [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle) -
          Performs various actios when lifecycle state changes
        * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Stores
          and manages UI-related data
    * [Room](https://developer.android.com/training/data-storage/room) - Stores contact infromation 
* Architecture
    * [MVVM](https://developer.android.com/jetpack/guide?gclid=CjwKCAiAvaGRBhBlEiwAiY-yMLJgFw8dtzM8r78wKMlnykKhTDwh5vx4ZOGqGBbXQ8PEFlYsS_b_oBoCRGoQAvD_BwE&gclsrc=aw.ds)
        - Makes it easier to write modular, reusable and easy to test code
    * [Android Architecture components](https://developer.android.com/topic/libraries/architecture)
    * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)

#### Getting started

1. Android Studio -> File -> New -> From Version control -> Git
2. Enter `https://github.com/jerimkaura/Contacts.git` into URL field

### Application screenshots

Splash Screen            |  Add Contact Screen            | Contacts Screen             | Single Contact.            | Update Contact
:-------------------------:|:-------------------------:|:-------------------------:|:-------------------------:|:-------------------------:
![Splash Screen ](images/splashscreen.png)  |  ![Contacts Screen](images/addcontact.png)|  ![All Contacts](images/allcontacts.png)|  ![Single Contact](images/singlecontact.png) |  ![Update Contact](images/updatecontact.png)


#### License

 ```
 MIT License
 
 Copyright (c) 2022 Jerim Kaura
 
 Permission is hereby granted, free of charge, to any person obtaining a copy of this software and 
 associated documentation files (the "Software"), to deal in the Software without restriction, including 
 without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell 
 copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to 
 the following conditions:
 
 The above copyright notice and this permission notice shall be included in all copies or substantial 
 portions of the Software.
 
 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT 
 LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN 
 NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
 WHETHER IN AN ACTION OF  TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE 
 SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ```
