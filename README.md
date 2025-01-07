# Amani SDK
![AmaniSDKHeader](https://user-images.githubusercontent.com/75306240/187691841-249b2773-7e55-44ba-b2c2-8ce263115f39.png)


# Table of Content
- [Overview](#overview)
- [Basics](#basics)
    - [General Requirements](#general-requirements)
    - [Compile Options](#compile-options)
    - [App Permissions](#app-permissions)
    - [Gradle Properties](#gradle-properties)
    - [ProGuard Rule Usage](#proguard-rule-usage)
- [Integration](#integration)
- [Amani SDK Usage](#amani-sdk-usage)
    - [Amani Initial-First Setup](#amani-initial-first-setup)
    - [BIO Login](#bio-login)
    - [ID Capture](#id-capture)
    - [Manual Selfie Capture](#manual-selfie-capture)
    - [Auto Selfie Capture](#auto-selfie-capture)
    - [Selfie Pose Estimation](#selfie-pose-estimation)
    - [Generic Document](#generic-document)
    - [Customer Update](#customer-update)
    - [Customer Detail](#customer-detail)
    - [Digital Signature](#digital-signature)
    - [NFC Reading](#nfc-reading)
    - [Video Call](#video-call)
- [CallBack Guideline](#callback-guideline)
    - [Upload CallBack](#upload-callback)
    - [Start CallBack](#start-callback)
    - [Exceptions](#exceptions)



# Overview

The Amani Software Development kit (SDK) provides you complete steps to perform KYC.This SDK
consists of 5 steps:

## 1. Upload Your Identification:

This internally consist of 4 types of documents, you can upload any of them to get your
identification verified. These documents are 1. Turkish ID Card(New): There you can upload your new
Turkish ID card. 2. Turkish ID Card(Old): There you can upload your old Turkish ID card. 3. Turkish
Driver License: There you can upload your old turkish driver license. 4. Passport: You can also
upload your passport to get verification of your identity.

## 2. Upload your Selfie:

This steps includes the taking a Selfie and uploading it.

## 3. Upload Your Proof of Address:

There we have 4 types of categories you can upload any of them to get your address verified.

1. Proof of Address: you will upload simply proof of address there.
2. ISKI: you will upload ISKI address proof there.
3. IGDAS: There you have the option of IGDAS.
4. CK Bogazici Elektrik: You have to upload the same here.

## 4. Sign Digital Contract:

In this step, you will enter your information required to make digital contract.Then you will got
your contract in the same step from our side.Then by reading that contract, you have to sign that
and then at the end upload the same.

## 5. Upload Physical Contract:

In this step, you will download your physical contract. Then you have to upload the same contrat by
filling the all the information to get your physical contract verified.

# Basics

## General Requirements

The minimum requirements for the SDK are:

* API Level 21
* compileSdkVersion 34

## Compile Options

```groovy
  compileOptions {
        sourceCompatibility JavaVersion.VERSION_17
        targetCompatibility JavaVersion.VERSION_17
    }
```

## App Permissions

Amani SDK makes use of the device Camera, Location and ScanNFC. If you dont want to use location
service please provide in init method. You will be required to have the following keys in your
application's Manifest file:

```xml
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
         android:maxSdkVersion="32" />
     <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"
         android:maxSdkVersion="32" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.NFC" />
```

## Gradle Properties

Disable R8 full mode, use AndroidX and enable Jetifier like below;

   ```properties
    android.enableR8.fullMode=false
    android.useAndroidX=true
    android.enableJetifier=true
 ```

## ProGuard Rule Usage ##

   * If you are using ProGuard in your application, you need to add this code block into your rules!

   ```groovy
-keep class ai.** {*;}
-dontwarn ai.**
-keep class datamanager.** {*;}
-dontwarn datamanager.**
-keep class networkmanager.** {*;}
-dontwarn networkmanager.**

-keep class org.jmrtd.** {*;}
-keep class net.sf.scuba.** {*;}
-keep class org.bouncycastle.** {*;}
-keep class org.spongycastle.** {*;}
-keep class org.ejbca.** {*;}

-dontwarn org.ejbca.**
-dontwarn org.bouncycastle.**
-dontwarn org.spongycastle.**
-dontwarn org.jmrtd.**
-dontwarn net.sf.scuba.**

-keep class org.tensorflow.lite**{ *; }
-dontwarn org.tensorflow.lite.**
-keep class org.tensorflow.lite.support**{ *; }
-dontwarn org.tensorflow.lite.support**

-dontwarn org.conscrypt.Conscrypt$Version
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.conscrypt.ConscryptHostnameVerifier
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE
   ```

## Integration

Dependencies:Dependencies:

1. Add the following dependencies to your Module build.gradle file.

```groovy
implementation 'ai.amani.android:AmaniAi:2.6.5'
```

### Example of usage:

```groovy
dependencies {
...
implementation 'ai.amani.android:AmaniAi:2.6.5' // Add only this line
...
}
```

2. Enable DataBinding in the Module build.gradle by adding this line into code block of android {}:

```groovy
dataBinding { enabled true }
```

### Example of usage

```groovy
android {
...

dataBinding { enabled true } // Add this line to enable data binding feature.
...
}
```

3. Add the following in the Project build.gradle within in buildscript within the buildscript->
   repositories and buildscript->allprojects.

```groovy
maven { url "https://jfrog.amani.ai/artifactory/amani-sdk"}
```

### Example of usage:

```groovy
allprojects {
repositories {
google()
jcenter()
maven {
url "https://jfrog.amani.ai/artifactory/amani-sdk"
}
}
}
```

# Amani SDK Usage

A sample application that calls Amani SDK functions properly.

## Amani Initial-First Setup

There are two different functions required in the setup. init() and initAmani(). The init() function provides the overall SDK installation before the application is even up. The initAmani() function can be used at any step/time when a user is desired to be created in the Amani system after the application is up. For the use of all other functions (uploads, fragmental view structures) the initAmani() function must return success.

### init() :
Before the application stands up, it is called once in the application layer, as in the example.

> **Warning**
> This init method must be called once before all other methods. This is required for installation of other methods. If other methods/modules are used without calling Init, you will get a ***RuntimeException("Amani not initialised")*** error. In such cases, make sure that Amani's init method is called.

> **Note**
> SHARED_SECRET is a key that ensures and validates the validity/security of the request in network requests. This key will be sent to you confidentially by the Amani team.
In cases where you do not provide SharedSecret, the Amani.init() method will still work without any problems. However, requests made in the upload methods will be unsigned. Use SharedSecret to avoid such security situations.

| Upload Sources      | String Equivalents |
|------------------------------|--------------------|
| UploadSource.KYC             | Kyc                |
| UploadSource.PASSWORD        | Password           |
| UploadSource.VIDEO           | Video              |
| UploadSource.PASSWORD_UPDATE | Password_update    |
| UploadSource.PHONE_UPDATE    | Phone_update       |

```kotlin
//Amani init with only server param.
//When sharedSecret is not given, validity/security of the requests will not be activated.
Amani.init(this,"SERVER")

//Amani init with server, sharedSecret, without UploadSource.
Amani.init(this,"SERVER", "SHARED_SECRET")

//Amani init with server, sharedSecret and UploadSource params.
//UploadSource use cases -> [UploadSource.KYC, UploadSource.PASSWORD, UploadSource.VIDEO, UploadSource.PHONE_UPDATE, PHONE_UPDATE.PASSWORD_UPDATE ].
Amani.init(this,"SERVER", "SHARED_SECRET", UploadSource.KYC)        
```

> **Note**
> UploadSource is used to distinguish uploads from different sources. Current supported upload source list is below. If UploadSource is not specified, the default is UploadSource.KYC. If you need to change UploadSource after init; You can also use the ***Amani.sharedInstance().setUploadSource()*** method.

> **Warning**
> To set upload source as string, be sure that you use the string equivalent from the table
```kotlin
//Setting the UploadSource as enum    
Amani.sharedInstance().setUploadSource(UploadSource.KYC)

//Setting the UploadSource as string
Amani.sharedInstance().setUploadSource("Kyc")

```

### initAmani():
After the application stands up, the initAmani() function can be called in the flow of a desired screen, depending on the result of initAmani() function other modules will be also available to use. If isSuccess is true as a result of the initAmani() request, other modules can be used. Otherwise, you will get an error from the callback result of the current module. It is enough for initAmani() to be successful once during the lifecycle. The application must be called again when the life cycle ends and a new life cycle starts. (In cases where the user closes and opens the application)

```kotlin
Amani.sharedInstance()
  .initAmani(
    this, 
    "ID_NUMBER", 
    "EMAIL", 
    "PASSWORD", 
    "LANGUAGE e.g. tr") 
  { isSuccess: Boolean, errorCode: Int? ->

    if (isSuccess) {
        //The request is successful, all other modules are ready to use.
    } else {
        //The request failed, due to error : $errorCode, other modules cannot
        //Other modules cannot be used until the request is successful.
    }
}
```

-----

## BIO Login

### Manual Selfie Capture Login

It includes the Fragment that allows the selfie to be taken manually from the user with the help of a button. The difference from Auto Selfie is that there is no button in Auto Selfie Capture and the selfie is taken automatically.

* BioLogin().ManualSelfieCapture() : Contains the necessary User Interface configurations for Manual Selfie

```kotlin
//Manual Selfie Capture User interface configuration
val fragment: Fragment? = Amani.sharedInstance()
  .BioLogin()
  .ManualSelfieCapture()
  .userInterfaceColors(
    appFontColor = R.color.white,
    manualButtonColor = R.color.white,
    ovalViewColor = R.color.white,
    appBackgroundColor = R.color.white
  )
  .userInterfaceTexts(
    "PLease be sure your face is inside the area!"
  )
  .observer(object : ManualSelfieCaptureObserver {
    override fun cb(bitmap: Bitmap?) {
      if (bitmap != null) {
        //Selfie data is taken, upload method can be triggered now to check result of BIOLogin.
        uploadSelfie()
      }
    }
  })
  .build()

//Navigating the fragment with null check
fragment?.let {
  replaceFragmentWithBackStack(R.id.frame_pose_estimation, it)
}        

```

### Auto Selfie Capture Login

Auto Selfie Capture provides you with UI fragment configurations that allow the user's Selfie to be taken automatically, and tracking the result of the Selfie with upload operations. In Auto Selfie Capture, if the selfie cannot be taken after the desired time, it offers fallback with the possibility of capturing with the manual button. The sharp difference between AutoSelfieCapture and ManualSelfieCapture is that it provides real-time tracking of faces on the screen. At the same time, it checks that the face is in the desired dimensions and distance.

* Add the following pom to the dependencies section of your gradle build ﬁle :

> **Warning**
> In order to use the AutoSelfieCapture module, the "tflite" definition below must be added as in the example below. If it is not added, you will get a compilation error.


```groovy
  aaptOptions {
    noCompress "tflite"
}
```

* BioLogin().AutoSelfieCapture() : Contains the necessary User Interface configurations for Auto Selfi Capture.

``` kotlin
//Auto Selfie Capture User interface configuration
val fragment = Amani.sharedInstance()
    .BioLogin()                                                                     
    .AutoSelfieCapture()                                                                                             
    .timeOutManualButton(30)                                                                                         
    .userInterfaceColors(                                                                                            
        ovalViewStartColor = R.color.white,                                                                          
        ovalViewSuccessColor = R.color.approve_green,                                                                
        appFontColor = R.color.white,                                                                                
        manualButtonColor = R.color.white                                                                            
    )                                                                                                                
    .                                                                                                                
    userInterfaceTexts(                                                                                              
        faceIsTooFarText = "Face is too far",                                                                        
        holdStableText = "Hold stable text",                                                                         
        faceNotFoundText = "Face not found text"                                                                     
    )
    .observer(object : AutoSelfieCaptureObserver {                                                                   
        override fun cb(bitmap: Bitmap?) {                                                                           
                                                                                                             
            if (bitmap != null) {                                                                                    
                //Selfie data is taken, upload method can be triggered now to check result of BIOLogin.              
                uploadSelfie()                                                                                       
            }                                                                                                        
        }                                                                                                            
                                                                                                             
    })                                                                                                               
    .build()                                                                                                         
                                                                                                             
//Navigating the fragment with null check                                                                            
fragment?.let {                                                                                                      
    replaceFragmentWithBackStack(R.id.frame_pose_estimation, it)                                                     
}                                                                                                                    
```

### Selfie Pose Estimation Login

The Selfie Pose Estimation module provides you with the UI fragment configurations that allow the user's Selfie to be taken automatically as a result of the successful instructions from the user, and the upload operations and the result of the Selfie to be followed.

Any number of poses can be retrieved from the user with the requestedPoseNumber() configuration. These poses are randomly determined as [UP, DOWN, RIGHT, LEFT] head movement.

The selected poses are randomly provided to the user, different from the previous pose. It is taken if the user's face is in suitable conditions and all poses are successful. And it can be uploaded with the upload function.

As a result of the upload function, the action of the Selfie result can be taken according to the Selfie's response result.

* Add the following pom to the dependencies section of your gradle build ﬁle :

> **Warning**
> In order to use the SelfiePoseEstimation module, the "tflite" definition below must be added as in the example below. If it is not added, you will get a compilation error.


```groovy
  aaptOptions {
    noCompress "tflite"
}
```

``` kotlin
val fragment: Fragment? = Amani.sharedInstance()
    .BioLogin()
    .PoseEstimation()                                        
    .requestedPoseNumber(1)                                                                                     
    .ovalViewAnimationDurationMilSec(12000)                                                                     
    .userInterfaceColors(                                                                                       
        appFontColor = R.color.your_color,                                                                      
        ovalViewStartColor = R.color.your_color,                                                                
        ovalViewSuccessColor =  R.color.your_color,                                                             
        ovalViewErrorColor = R.color.your_color,                                                                
        alertFontTitleColor = R.color.your_color,                                                               
        alertFontDescriptionColor = R.color.your_color,                                                         
        alertFontTryAgainColor = R.color.your_color,                                                            
        alertBackgroundColor = R.color.your_color                                                               
    )                                                                                                           
    .userInterfaceTexts(                                                                                        
        faceNotInside = "Face is not inside",                                                                   
        faceNotStraight = "Face is not straight",                                                               
        faceIsTooFar = "Face is too far",                                                                       
        holdPhoneVertically = "Hold phone as vertical",                                                         
        alertTitle = "Verification Failed",                                                                     
        alertDescription = "Verification is failed, please try again.",                                         
        alertTryAgain = "Try again",                                                                            
        turnDown = "Turn your head down",                                                                       
        turnLeft = "Turn your head left",                                                                       
        turnRight = "Turn your head right",                                                                     
        turnUp = "Turn your head up",                                                                           
        faceStraight = "Look as straight position to screen"                                                    
    )                                                                                                           
    .userInterfaceDrawables(                                                                                    
        mainGuideStraight = R.drawable.your_drawable,                                                           
        mainGuideUp = R.drawable.your_drawable,                                                                 
        mainGuideRight = R.drawable.your_drawable,                                                              
        mainGuideLeft = R.drawable.your_drawable,                                                               
        mainGuideDown = R.drawable.your_drawable,                                                               
        secondaryGuideUp = R.drawable.your_drawable,                                                            
        secondaryGuideRight = R.drawable.your_drawable,                                                         
        secondaryGuideDown = R.drawable.your_drawable,                                                          
        secondaryGuideLeft = R.drawable.your_drawable                                                           
    )                                                                                                           
    .userInterfaceVisibilities(                                                                                 
        mainGuideVisibility = true,                                                                             
        secondaryGuideVisibility = true                                                                         
    )
    .observer(object : PoseEstimationObserver{                                                                  
       override fun onSuccess(bitmap: Bitmap?) {                                                                
           if (bitmap != null) {                                                                                
               //Selfie data is taken, upload method can be triggered now to check result of BIOLogin.          
               uploadSelfie()                                                                                   
           }                                                                                                    
       }                                                                                                        
                                                                                                                
       override fun onFailure(reason: OnFailurePoseEstimation, currentAttempt: Int) {                         
           //Pose on failure, poseNumber : $currentAttempt                                                      
       }                                                                                                        
                                                                                                                
       override fun onError(error: Error) {                                                                   
           //On general error                                                                                   
       }                                                                                                        
                                                                                                                
   })                                                                                                         
    .build()                                                                                                    
                                                                                                                
//Navigating the fragment with null check                                                                     
fragment?.let {                                                                                                 
    replaceFragmentWithBackStack(R.id.frame_pose_estimation, it)                                                
}                                                                                                               
```


### Common

It contains methods/functions common to three modules [Manual Selfie Capture, Auto Selfie Capture, Selfie Pose Estimation].

The upload function is called when the status of the relevant module is successful. According to the model returned as a result of the upload request, it is understood whether BIOLogin is successful or not.

* upload() : Uploading the desired Selfie data from related module. Usage is below;

> **Warning**
> The relevant upload method has to be called after one of the modules from BIOLogin is success (ManualSelfieCapture, AutoSelfieCapture, SelfiePoseEstimation). Otherwise, you will encounter an error with the Error() object after the upload function is called.

```kotlin
Amani.sharedInstance()                                          
      .BioLogin()                                             
          .upload(                                                
              docType = "type_of_document example: {XXX_SE_0}",   
              token = TOKEN,                                      
              customerID = 9999, //Customer ID as Integer         
              object : BioLoginUploadCallBack {                   
          override fun cb(result: Boolean?, errors: Errors?) {    
              //Result must be TRUE for Success                   
          }                                                       
      })                                                          
```



-----

## ID Capture

The IDCapture module offers you the opportunity to automatically capture the document and upload it as a result. According to the upload result, you can listen to whether the document is approved or not.

* setManualCropTimeOut() : Sets the manual button of IDCapture for the situations when the AutoCapture of document is failed. When the manual crop button is enabled, the user will have chance to capture it manually for unexpected situations. Default manual crop time out value is defined as 30 seconds. If you did not call this method, it will keep going to use default value. Example usage is below;

> **Note**
> setManualCropTimeOut() -> Default Value is 30 seconds



```kotlin
        Amani.sharedInstance().IDCapture().setManualCropTimeOut(30)

```


* start() : Returns a Fragment of desired module of IDCapture
``` kotlin
        val fragment: Fragment? = Amani.sharedInstance()
        .IDCapture()
        .onException(onException = object : ExceptionCallBack{
            override fun onException(exception: Exception) {
                /**
                 * Triggering this block indicates that there is a non-fatal error caught in the
                 * fragment that will not crash your application.
                 *
                 * In such cases, you can log the error to your crash service to report it to
                 * the Amani Mobile team later.
                 *
                 * Then, you can send a message to the user informing them of the error and
                 * remove the fragment from the back stack with your fragment manager.
                 */
            }
        })
        .start(
            this, //Context of the Application
            frameLayout, //FrameLayout where the current IDCapture fragment will be fit in
            docType, // Type of the document
            false // Boolean value for frontSide/backSide, set true for frontSide, set false for backSide
        ) { bitmap: Bitmap?, b: Boolean?, file: File? ->
            bitmap?.let {
                //Current document is captured, upload method of current module can be triggered.
            }
        }
        replaceFragmentWithBackStack(R.id.id_frame_layout, fragment!!)
```

* upload() : Uploading the desired IDCapture data from related module to check result of uploaded document. Usage is below;

> **Warning**
> The relevant upload method has to be called after both the front and back sides of the document are captured. Otherwise, you will encounter an error with the Error() object after the upload function is called.

``` kotlin
     Amani.sharedInstance()
     .IDCapture()
     .upload(
            this, // Context of Application/Activity
            "type_of_document", //Type of current document
            object : IUploadCallBack{
                override fun cb(isSuccess: Boolean, result: String?, errors: MutableList<Errors>?) {
                }
            }
        )
```

* getMRZ() : Fetches the data group for required scanning NFC.

> **Note**
> This method is used to capture the NFC Data group required for NFC scanning in cases where only the back side is taken. It is optional, you do not need this method if you have the data group required for NFC.

```kotlin
Amani.sharedInstance()
  .IDCapture()
  .getMRZ(                                                                                     
    type = "type_of_document",                                                                                                         
    onComplete = { mrz ->                                                                                                              
        /* Mrz model will be fetched in this section. This data model contains required values                                         
        for scanning NFC.                                                                                                              
                                                                                                                                       
        For scanning NFC with the response of Mrz request, the following values can be used in ScanNFC.start(_,_,_) function ->
                                                                                                                                       
            mrz.mrzBirthDate,                                                                                                  
            mrz.mrzExpiryDate,                                                                                                         
            mrz.mrzDocumentNumber                                                                                                      
                                                                                                                                       
        */                                                                                                                     
                                                                                                                                       
    },                                                                                                                         
    onError = {                                                                                                                        
       //Error handling here                                                                                                                              
    }                                                                                                                                                                                                                                                           
)                                                                                                                              
        

```

-----

## Manual Selfie Capture

It includes the Fragment that allows the selfie to be taken manually from the user with the help of a button. The difference from Auto Selfie is that there is no button in Auto Selfie Capture and the selfie is taken automatically.

* start() : Returns a Fragment of desired module of ManualSelfieCapture

```kotlin
//Getting the Fragment view of Selfie                                                           
 val fragment = Amani.sharedInstance()                                                              
        .Selfie()
        .onException(onException = object : ExceptionCallBack{
            override fun onException(exception: Exception) {
                /**
                 * Triggering this block indicates that there is a non-fatal error caught in the
                 * fragment that will not crash your application.
                 *
                 * In such cases, you can log the error to your crash service to report it to
                 * the Amani Mobile team later.
                 *
                 * Then, you can send a message to the user informing them of the error and
                 * remove the fragment from the back stack with your fragment manager.
                 */
            }
        })
        .start(                                                                                     
            "type_of_document example: XXX_SE_0",                                                   
            object : IFragmentCallBack{                                                             
                override fun cb(bitmap: Bitmap?, manualButtonActivated: Boolean?, file: File?) {    
                    //If bitmap is not null it means Selfie is taken successfuully                  
                }                                                                                   
            }                                                                                       
        )                                                                                           
                                                                                                    
    //Navigating the desired Fragment view                                                      
    fragment?.let {                                                                                 
        replaceFragmentWithBackStack(R.id.frame_pose_estimation, it)                                
    }                                                                                               
```

* upload() : Uploads the taken data from ManualSelfie

> **Warning**
> The upload method has to be called after the result of the relevant module is successful. Otherwise, you will encounter an error in the Errors() object when called.

```kotlin
//Uploading the current taken Selfie data
Amani.sharedInstance()                                                                     
    .Selfie()                                                                              
    .upload(                                                                               
        this,                                                                              
        "type_of_document example: XXX_SE_0",                                              
        object : IUploadCallBack{                                                          
            override fun cb(                                                               
                isSuccess: Boolean,                                                        
                result: String?,                                                           
                errors: MutableList<Errors>?                                               
            ) {                                                                            
                //isSuccess means the Selfie Data is uploaded                              
                //result is the result of uploaded document it should be equal "OK"        
                // if the current Selfie has no error                                      
                //If Selfie has errors, errors object will not be null anymore             
            }                                                                              
        }                                                                                  
    )                                                                                      
```
-----

## Auto Selfie Capture

Auto Selfie Capture provides you with UI fragment configurations that allow the user's Selfie to be taken automatically, and tracking the result of the Selfie with upload operations. In Auto Selfie Capture, if the selfie cannot be taken after the desired time, it offers fallback with the possibility of capturing with the manual button. The sharp difference between AutoSelfieCapture and ManualSelfieCapture is that it provides real-time tracking of faces on the screen. At the same time, it checks that the face is in the desired dimensions and distance.

* Add the following pom to the dependencies section of your gradle build ﬁle :

> **Warning**
> In order to use the AutoSelfieCapture module, the "tflite" definition below must be added as in the example below. If it is not added, you will get a compilation error.


```groovy
aaptOptions {
    noCompress "tflite"
}
```

* start() : Returns the relavant Fragment of the Auto Selfie Capture

``` kotlin
// Preparing AutoSelfieCaptureBuilder for User Interface
 val ascBuilder =  ASCBuilder(
                  R.color.color_black, // Text color of message
                  20, // Text size of message
                  R.color.any_color, // Text color of counter animation.
                  true, // Counter visibility as boo
                  100, // Counter text size (It should be proportional to the screen size. As a proper usage: getScreenHeight()/10)
                  20, // Manual capture button time out as second.
                  "Be sure to be close enough", // Message texts
                  "Face not found", // Message texts
                  "Hold stable, while taking photo", // Message texts
                  "Failed, process will restart in 3 seconds", // Message texts
                  R.color.any_color2, // Oval view color
                  R.color.any_color3) // Success animate color

//Getting the view of related Fragment
Amani.sharedInstance()                                                                              
        .AutoSelfieCapture()
        .onException(onException = object : ExceptionCallBack{
            override fun onException(exception: Exception) {
                /**
                 * Triggering this block indicates that there is a non-fatal error caught in the
                 * fragment that will not crash your application.
                 *
                 * In such cases, you can log the error to your crash service to report it to
                 * the Amani Mobile team later.
                 *
                 * Then, you can send a message to the user informing them of the error and
                 * remove the fragment from the back stack with your fragment manager.
                 */
            }
        })
        .start(                                                                                     
            docType = "type_of_document example: XXX_SE_0",                                         
            ascBuilder = ascBuilder,                                                                
            frameLayout = frameLayout,                                                              
            object : IFragmentCallBack {                                                            
                override fun cb(bitmap: Bitmap?, manualButtonActivated: Boolean?, file: File?) {    
                    //If bitmap is not null it 's succeed                                           
                }                                                                                                                                                                                   
            }                                                                                                                                                                         
        )                                                                                           
                                                                                                
//Navigating the desired Fragment view
fragment?.let {                                                          
    replaceFragmentWithBackStack(R.id.frame_pose_estimation, it)         
}                                                                        
```

* upload() : Uploads the taken data from AutoSelfieCapture

> **Warning**
> The upload method has to be called after the result of the relevant module is successful. Otherwise, you will encounter an error in the Errors() object when called.

```kotlin
//Uploading the current taken Selfie data
Amani.sharedInstance()                                                                     
    .AutoSelfieCapture()                                                                   
    .upload(                                                                               
        this,                                                                              
        "type_of_document example: XXX_SE_0",                                              
        object : IUploadCallBack{                                                          
            override fun cb(                                                               
                isSuccess: Boolean,                                                        
                result: String?,                                                           
                errors: MutableList<Errors>?                                               
            ) {                                                                            
                //isSuccess means the Selfie Data is uploaded                              
                //result is the result of uploaded document it should be equal "OK"        
                // if the current Selfie has no error                                      
                //If Selfie has errors, errors object will not be null anymore             
            }                                                                              
        }                                                                                  
    )                                                                                      
```

-----
## Selfie Pose Estimation
Pose Estimation creates a view that asks the user for random and certain number of head movements to confirms them from camera stream at run time that you can observer it with the customer observer pattern. You can use this view fragmentally as in other chapters.

* Add the following pom to the dependencies section of your gradle build ﬁle :

> **Warning**
> In order to use the SelfiePoseEstimation module, the "tflite" definition below must be added as in the example below. If it is not added, you may get a compilation error.


```groovy
  aaptOptions {
    noCompress "tflite"
}
```

```kotlin
// Creating custom Selfie Poese Estimation fragment to navigate it                              
val fragment = Amani.sharedInstance().SelfiePoseEstimation()                                        
        .Builder()                                                                                  
        .requestedPoseNumber(1)                                                                     
        .ovalViewAnimationDurationMilSec(500)                                                       
        .observe(observer)                                                                          
        .userInterfaceColors(                                                                       
            appFontColor = R.color.your_color,                                                      
            ovalViewStartColor = R.color.your_color,                                                
            ovalViewSuccessColor =  R.color.your_color,                                             
            ovalViewErrorColor = R.color.your_color,                                                
            alertFontTitleColor = R.color.your_color,                                               
            alertFontDescriptionColor = R.color.your_color,                                         
            alertFontTryAgainColor = R.color.your_color,                                            
            alertBackgroundColor = R.color.your_color                                               
        )                                                                                           
        .userInterfaceTexts(                                                                        
            faceNotInside = "Face is not inside",                                                   
            faceNotStraight = "Face is not straight",                                               
            faceIsTooFar = "Face is too far",                                                       
            holdPhoneVertically = "Hold phone as vertical",                                         
            alertTitle = "Verification Failed",                                                     
            alertDescription = "Verification is failed, please try again.",                         
            alertTryAgain = "Try again",                                                            
            turnDown = "Turn your head down",                                                       
            turnLeft = "Turn your head left",                                                       
            turnRight = "Turn your head right",                                                     
            turnUp = "Turn your head up",                                                           
            faceStraight = "Look as straight position to screen"                                    
        )                                                                                           
        .userInterfaceDrawables(                                                                    
            mainGuideStraight = R.drawable.your_drawable,                                           
            mainGuideUp = R.drawable.your_drawable,                                                 
            mainGuideRight = R.drawable.your_drawable,                                              
            mainGuideLeft = R.drawable.your_drawable,                                               
            mainGuideDown = R.drawable.your_drawable,                                               
            secondaryGuideUp = R.drawable.your_drawable,                                            
            secondaryGuideRight = R.drawable.your_drawable,                                         
            secondaryGuideDown = R.drawable.your_drawable,                                          
            secondaryGuideLeft = R.drawable.your_drawable                                           
        )                                                                                           
        .userInterfaceVisibilities(                                                                 
            mainGuideVisibility = true,                                                             
            secondaryGuideVisibility = true                                                         
        )
        .onException(onException = object : ExceptionCallBack{
            override fun onException(exception: Exception) {
                /**
                 * Triggering this block indicates that there is a non-fatal error caught in the
                 * fragment that will not crash your application.
                 *
                 * In such cases, you can log the error to your crash service to report it to
                 * the Amani Mobile team later.
                 *
                 * Then, you can send a message to the user informing them of the error and
                 * remove the fragment from the back stack with your fragment manager.
                 */
            }
        })                                                                                                                                                                                     
        .build(this)                                                                                
                                                                                                    
 // Navigating the fragment with null check                                                     
 if(fragment != null) {                                                                             
     navigatetFragmentMethod(fragment);                                                             
 }                                                                                                  
                                                                                                    
// Creating the observable to observe PoseEstimation events                                     
private val observable: PoseEstimationObserver = object : PoseEstimationObserver {                  
    override fun onSuccess(bitmap: Bitmap?) {                                                       
        bitmap?.let {                                                                               
            Log.i(TAG, "Verification is success")                                                   
            uploadSelfie()                                                                          
        }?: run {                                                                                   
            Log.e(TAG, "Verification is not success")                                               
        }                                                                                           
    }                                                                                               
                                                                                                    
    override fun onFailure(reason: OnFailurePoseEstimation, currentAttempt: Int) {              
        Log.d(TAG, "onFailure: $currentAttempt")                                                    
    }                                                                                               
                                                                                                    
    override fun onError(error: Error) {                                                        
        Log.e(TAG, "onError: $error" )                                                              
    }                                                                                               
}                                                                                                  
```

* upload() : Uploads the taken data from SelfiePoseEstimation

> **Warning**
> The upload method has to be called after the result of the relevant module is successful. Otherwise, you will encounter an error in the Errors() object when called.

```kotlin
//Uploading the current taken Selfie data
Amani.sharedInstance()                                                                          
    .SelfiePoseEstimation()                                                                     
    .upload(                                                                                    
        this,                                                                                   
        "type_of_document example: XXX_SE_0",                                                   
        object : IUploadCallBack{                                                               
            override fun cb(                                                                    
                isSuccess: Boolean,                                                             
                result: String?,                                                                
                errors: MutableList<Errors>?                                                    
            ) {                                                                                 
                //isSuccess means the Selfie Data is uploaded                                   
                //result is the result of uploaded document it should be equal "OK"             
                // if the current Selfie has no error                                           
                //If Selfie has errors, errors object will not be null anymore                  
            }                                                                                   
        }                                                                                       
    )                                                                                           
```
-----

## Generic Document

This chapter provides clipping and uploading of generic documents by adhering to the desired
document type, number and screen configuration. Document type specifies the type of document to be
loaded and the document builder specifies the display configurations (colors, texts, documentCount:
how many document will be taken from camera.) in it.

* Supported file type list; file.pdf, file.jpg, file.png, file.webp, file.bmp

* Preparing DocumentBuilder

``` java
// Preparing DocumentBuilder for User Interface && Some Logic Operations
DocBuilder docBuilder = new DocBuilder("Tekrar Dene", // Text label         
               "Onayla", // Text label                                      
               R.color.color_blue, // Text color                            
               R.color.color_blue, // Text color                            
               R.color.white, // Button color                               
               2); // Indicates the number of documents.                    
```

* Replacing Fragment and Uploading Data from Camera

``` java
//Calling Generic Document Fragment and Uploading Documents (due to lambda function result)
Fragment docFragment = Amani.sharedInstance()
  .Document()
  .start("DOC_TYPE",docBuilder, frameLayout,(docList,isSucess)->{

            if (isSuccess) {
                //Uploading Generic Documents that taken from camera
                Amani.sharedInstance().Document().upload(this,"DOC_TYPE",(isSuccess, result, errors) -> {
                    if (isSuccess) Toast.makeText(this,"Generic Document Upload is Success", Toast.LENGTH_LONG).show();
                    else Toast.makeText(this,"Generic Document Upload is Not Success", Toast.LENGTH_SHORT).show();
                });
            }
        });
        yourFragmentMethod(docFragment);
```

* Uploading Generic Documents

``` java
// Uploading Generic Documents that taken as ByteArray (Not from camera, will be provided from as static data as ArrayList of
//FileWithType data class that will be provided from AmaniSDK.)

FileWithType docData1 = new FileWithType(byteArrayOfPdf,"pdf"); // An object data class first param: byteArray, second param: type (file type: jpeg,pdf,png etc.)
FileWithType docData2 = new FileWithType(byteArrayOfJpeg, "jpeg");

ArrayList<FileWithType> docDataList = new ArrayList<>(); // List of files that will be given to upload.
docDataList.add(docData1); // Adding first document data
docDataList.add(docData2); // Second document data
... // Up to 10 document uploads have been tested without any problems.

Amani.sharedInstance().Document().upload(this,"TUR_ID_1",docDataList,(iSuccess,result,listOfErrors)->{
            if (isSuccess)//Upload is SUCCESS!
        });
```

* Deleting Document Cache Files (Optional usage, NOT Required!)

``` java
// It provides to delete all files that taken from camera.
// It must be called when the upload process is done.
// (You can check nullability of "isSucess: Boo" in Document.start() lambda
// expression callback to understand process of upload is done or not!)
// Otherwise upload will be failed.
// Also you can delete the files from given file path by yourself.
// It means no need to use it.
Amani.sharedInstance().Document().deleteAllDocumentCaches();
```
-----

## Customer Update

* It basically contains two functions, setCustomerInfo() and upload() function. The setInfo()
  function is a function that records the basic information of the user, and it must be called
  before upload() function called.

``` java
Amani.sharedInstance()
  .CustomerInfo()
  .setInfo(
    "Mobile Developer",
    "İstanbul",
    "Maslak",
    "Maslak Mah. ...");
```
* upload() function example usage;

``` java
Amani.sharedInstance()
  .CustomerInfo()
  .upload((isSuccess, result, errors) -> {               
        if (isSuccess) // Uploading CustomerInfo is successfull.                            
        if (result.equals("OK")) // Pending review.                                         
        if (errors != null) if (errors.get(0) != null){                                     
        // There is an error.                                                               
        }                                                                                   
  });                                                                                 
```
-----

## Customer Detail

It is used to get information about the last updated details about the created customer.

``` java
Amani.sharedInstance()
  .CustomerDetail()
  .getCustomerDetail
  (new CustomerDetailObserver() {                    
      @Override                                                                                                  
      public void result(@Nullable ResCustomerDetail customerDetail, @Nullable Throwable throwable) {       
          //customerDetail is fetched if there is no throwable                                              
      }                                                                                                     
  });                                                                                                   
```
-----

## Digital Signature

Signature() method basically contains 5 inner methods; start() , upload(), clean(), confirm(),
resetCountOfSignature().

* start(): Return type is fragment.

* clean(): Void function, cleans the taken signature from canvas.

* upload(): Void function, uploads the taken signature/signatures from canvas. It must be called
  when the number of required signature is reached.

* confirm(): Void function that allows to get the next signature.

* resetCounfOfSignature(): The void function resets the number of signed signatures. It can be used
  when necessary. It's use is optional.

``` java
//Initiliazing Amani SDK (WARNING! This method must be called at least once before other methods are called in same activity. If you in another acitivity you may need to call it twice.)
Amani.init(MainActivity.this, "SERVER", "SHARED_SECRET"); // SHARED_SECRET is a Key required to sign Signature for security layer.
```

* start() function example usage;

``` java
int NUMBER_OF_SIGNATURE_WILL_BE_TAKEN = 1;
Amani.sharedInstance().Signature().start(this, NUMBER_OF_SIGNATURE_WILL_BE_TAKEN, (bitmap, countOfSignature) -> {
    if (bitmap != null) {
        if (NUMBER_OF_SIGNATURE_WILL_BE_TAKEN == countOfSignature) {
            Amani.sharedInstance().Signature().upload((isSuccess, result, errors) -> {
                if (isSuccess) {
                    Log.d(TAG, "Signature is uploaded");
                }
                if (result != null) {
                    if (result.equals("OK")) {
                        Log.d(TAG, "Pending Review");
                    }
                }
                if (errors != null) {
                    if (errors.get(0) != null) {
                        Log.d(TAG, " Error" + errors.get(0));
                    }
                }
            });
        }
    }
});
```


* clean() function example usage;

``` java
Amani.sharedInstance().Signature().clean();
```

* upload() function example usage;

``` java
Amani.sharedInstance()
  .Signature()
  .upload { isSucess: Boolean, result: String?, errors: List<Errors?>? ->       
     Log.d(TAG, "upload isSuccess $isSucess")                                                                   
     Log.d(TAG, "upload result $result")                                                                        
    if(errors!= null) Log.d(TAG, "upload first error${errors[0]}")                                              
 }                                                                                                              
```


* confirm() function example usage;

``` java
Amani.sharedInstance().Signature().confirm(context);
```

* resetCountOfSignature() function example usage;

``` java
Amani.sharedInstance().Signature().resetCountOfSignature();
```
-----

## NFC Reading

For scanning NFC, will need two override methods. onNewIntent and onResume methods is required.
Usage is below.

``` java
//Initiliazing Amani SDK (WARNING! This method must be called at least once before other methods are called in same activity. If you in another acitivity you may need to call it twice.)
Amani.init(MainActivity.this, "SERVER", "SHARED_SECRET"); // SHARED_SECRET is a Key required to sign Signature for security layer.
```

``` java

//Starting ScanNFC Scanning

@Override protected void onResume() {
super.onResume();
ScanNFCAdapter adapter = ScanNFCAdapter.getDefaultAdapter(this);
if (adapter != null) {
Intent intent = new Intent(getApplicationContext(), this.getClass());
intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
String[][] filter = new String[][]{new String[]{"android.ScanNFC.tech.IsoDep"}};
adapter.enableForegroundDispatch(this, pendingIntent, null, filter);}
}

@Override protected void onNewIntent(Intent intent) { super.onNewIntent(intent); tag = intent.getExtras().getParcelable(ScanNFCAdapter.EXTRA_TAG);
Amani.sharedInstance().ScanNFC().start(tag, getApplicationContext(),
"BIRTH DATE" ,
"EXPIRE DATE" ,
"DOCUMENT NUMBER",
(bitmap, isSuccess, exception) -> {if (isSuccess) {//ScanNFC Scan is success! }); }
```

```java
Amani.sharedInstance()
    .ScanNFC()
    .upload(this,"DOCUMENT TYPE", (uploadNFCSuccess, resultNFC, errors) ->
    {
        
    }); // It must call if ScanNFC is success.
```
-----

## Video Call
This method, which has a single sub-function, returns an intent. When you call the intent, the call starts in the chrome application, if there is no call, you can catch it from the exception.
After the initAmani method is called and the login process is successful, it should be used as in the example.

``` java
Amani.sharedInstance().initAmani(MainActivity.this,"ID NUMBER","TOKEN","tr",(isSuccess, errorCode) -> {
            if (isSuccess){
                //Login successful
                try {
                    startActivity(Amani.sharedInstance().VideoCall()
                    .geoLocation(true)
                    .language("tr")
                    .returnURL("SERVER_URL")
                    .start())
                } catch (ActivityNotFoundException e) {
                    Log.e("TAG", "Chrome not found: ");
                    // You can set any alert message to download chrome
                }
            }
        });
```
-----

# CallBack Guideline

There is a CallBack as the return type of the start and upload methods. This CallBack usually gives
you 2 parameters. You can find out what these parameters mean below.

- You can change the names of these parameters as you wish.

## Upload CallBack:

```java
//The method in front of the upload() method may vary according to the document as follows.
Amani.sharedInstance().ScanNFC()/IDCapture()/Selfie().upload(this,"DOCUMENT TYPE", (uploadNFCSuccess, resultNFC, errors) ->
{ } );
```

- uploadNFCSuccess (type: boolean): The first parameter of the upload method is always set in a
  boolean sense. Indicates that the upload was successful/failed as true/false.
- resultNFC (type: String): The second parameter of the upload method is the string of the message
  returned from the service after the upload process is completed. The cases where this message
  returns "OK" are returned when the service accepts this document.
- errors (type: ArrayList): It contains errorMessages, errorCodes if exists.

## Start CallBack:

The return CallBack of the start function contains one parameter, except NFC, and two in NFC. The
meanings of these parameters are below.

```java
//The method in front of the start() method may vary according to the document as follows.
Amani.sharedInstance().ScanNFC()/IDCapture()/Selfie().start(tag, getApplicationContext(),
"BIRTH DATE" ,
"EXPIRE DATE" ,
"DOCUMENT NUMBER",
(bitmap, isSuccess) -> { }
```

- bitmap (type: Bitmap) : If this parameter is not "null", it means that the result of the previous
  function was successful.

- isSuccess (type: boolean) : Indicates whether the operation was successful or unsuccessful.

## Exceptions:

### NFC Exception:

```java
...NFC().start("firstParam","secondParam,"thirdParam",(bitmap, isSuccess, exception))
```

The exception string in the NFC function's return callback returns an error message. You can find
these messages below.


### NFC Exception Messages:

| Exceptions | Possible Cases  |
| ------- | --- |
| "Tag was lost" | Situations such as moving the ID during scanning or interrupting the scanning. |
| "Failed to connect" | In cases where the connection is disconnected or the ID is removed from the device for some reason during the initial scan startup. |
| "Invalid key" | Using incorrect MRZ data or incorrect input states. |
| "General exception" | Instant or general exceptions due to device or ID card. |
| "Biometric photo not found" | Situations such as the inability to receive the biometric photo from the NFC chip due to identity or device. |


### General SDK Exceptions:

| Exception Codes | Cases  |
| ------- | --- |
| 10500 | No Connectivity Exception |
| 10505 | General Connection Error |
| 10506 | AppConfig Error |
| -HttpErrorCodes- | Http Response Exception |
-----




