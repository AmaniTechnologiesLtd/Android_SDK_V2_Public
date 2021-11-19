
# Amani SDKAmani SDK

# Table of ContentTable of Content

```
Overview
-Basics
-General Requirements
-Permissions
-Integration
 -Usage
 -Param Guideline
```
# OverviewOverview

The Amani Software Development kit (SDK) provides you complete steps to perform KYC.This SDK consists of 5 steps:

## 1. Upload Your Identification:

This internally consist of 4 types of documents, you can upload any of them to get your identification verified. These documents are 1. Turkish ID Card(New): There you
can upload your new Turkish ID card. 2. Turkish ID Card(Old): There you can upload your old Turkish ID card. 3. Turkish Driver License: There you can upload your old
turkish driver license. 4. Passport: You can also upload your passport to get verification of your identity.

## 2. Upload your Selfie:

This steps includes the taking a Selfie and uploading it.

## 3. Upload Your Proof of Address:

There we have 4 types of categories you can upload any of them to get your address verified.

1. Proof of Address: you will upload simply proof of address there.
2. ISKI: you will upload ISKI address proof there.
3. IGDAS: There you have the option of IGDAS.
4. CK Bogazici Elektrik: You have to upload the same here.

## 4. Sign Digital Contract:

In this step, you will enter your information required to make digital contract.Then you will got your contract in the same step from our side.Then by reading that
contract, you have to sign that and then at the end upload the same.

## 5. Upload Physical Contract:

In this step, you will download your physical contract. Then you have to upload the same contrat by filling the all the information to get your physical contract verified.

## Congratulation Screen:

After successfully uploading of all the documents you will see a congratulation screen saying you completed all the steps.We will check your documents and increase
your limit in 48 hours.

# Basics

## General Requirements

The minimum requirements for the SDK are:
* API Level 21

## App permissions

Amani SDK makes use of the device Camera, Location and ScanNFC. If you dont want to use location service please provide in init method. You will be required to
have the following keys in your application's Manifest file:


```
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.ScanNFC" />
```
NoteNote: All keys will be required for app submission.

### Grant accesss to ScanNFCGrant accesss to ScanNFC

Enable the Near Field Communication Tag Reading capability in the target Signing & Capabilities.

## Integration

Dependencies:Dependencies:


1. Add the following dependencies to your Module build.gradle file.

```
implementation 'ai.amani.android:AmaniAi:2.1.0'
```
### Example of usage:

```
dependencies {
...
implementation 'ai.amani.android:AmaniAi:2.1.0' // Add only this line
...
}
```
2. Enable DataBinding in the Module build.gradle by adding this line into code block of android {}:

```
dataBinding { enabled true }
```
### Example of usage

```
android {
...

dataBinding { enabled true } // Add this line to enable data binding feature.
...
}
```
3. Add the following in the Project build.gradle within in buildscript within the buildscript->repositories and buildscript->allprojects.

```
maven { url "https://jfrog.amani.ai/artifactory/amani-sdk"}
```
### Example of usage:


```
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
## Usage

Sample UsageSample Usage

A sample application that calls Amani SDK functions properly.

#### Usage of BIO Login


```java
//Initiliazing BIO Login SDK
Amani.sharedInstance().BioLogin().setParams(getApplicationContext(),"SERVER" );
```
``` java
//Calling BioLogin Selfie Fragment
Amani.sharedInstance().BioLogin().setParams(getApplicationContext(),"SERVER", "CUSTOMER ID" );
```
```java
Fragment fragment = Amani.sharedInstance().BioLogin().start("XXX_SE_0", bitmap -> {
if (bitmap != null) {
SelfieBm = bitmap;
Intent previewActivity = new Intent(getApplicationContext(), PreviewActivity.class);
startActivity(previewActivity);
}
});
replaceFragment(frameLayout,fragment,"YOUR TAG"); // replaceFragment is a public method to open any fragments.
```
``` java
//Uploading BioLogin datas.
Amani.sharedInstance().BioLogin().upload(PreviewActivity.this, "XXX_SE_0",
"TOKEN",
"CUSTOMER ID", (isSuccess, result) -> {
if (isSuccess) {
// Upload is successfull!
}
else {
// Failed!
}

if(result) {
// Successfuly login!
}
else {
//BioLogin is not successful!
}
});
```
```java
//Fragment call method. (You can also use your own method to call fragments as you wish.)
public void replaceFragment(FrameLayout frameLayout, Fragment fragment, String TAG){
MainActivity.this.getSupportFragmentManager().beginTransaction()
.replace(frameLayout.getId(), fragment, TAG)
.addToBackStack(null)
.commit();
}
```
#### Usage of ID Capture

```java
//Initiliazing Amani SDK
Amani.init(MainActivity.this, "SERVER", "VERSION");
```
``` java
Amani.sharedInstance().initAmani(MainActivity.this,"ID NUMBER","TOKEN","tr",(isSuccess, errorCode) -> {
if (isSuccess) //Login successful if it's true
else // Login is not successful
});
```
```java
//Calling ID Capture fragment
Fragment fragment = Amani.sharedInstance().IDCapture().start(MainActivity.this,"YOUR FRAME LAYOUT","DOC TYPE","FRONT SIDE = true / BACK SIDE = false", bitmap -> {

if (bitmap! null) // ID Capture is done!
);

yourFragmentMethod(fragment);
```
#### Usage of Selfie

```java

//Calling Selfie Fragment 
Fragment fragment = Amani.sharedInstance().Selfie().start("XXX_SE_0", bitmap -> { if (bitmap != null) { // Selfie is successfully captured! } });

yourFragmentMethod(fragment);

```
```java
//Uploading Selfie Datas 
Amani.sharedInstance().Selfie().upload("ACTIVITY", "DOCUMENT TYPE", (isSuccess, result) -> { if (isSuccess) //Upload is SUCCESS! });
```
#### Usage of AutoSelfieCapture

```java
// Preparing AutoSelfieCaptureBuilder for User Interface
ASCBuilder ascBuilder = new ASCBuilder(
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
                  R.color.any_color3); // Success animate color 

//Calling Selfie Fragment 
Fragment fragment = Amani.sharedInstance().AutoSelfieCapture().start("XXX_SE_0", ascBuilder, frameLayout,(bitmap, onDestroyed, file) -> {
if (bitmap != null) { // Selfie is successfully captured! } });

yourFragmentMethod(fragment);

```
```java
//Uploading Selfie Datas 
Amani.sharedInstance().AutoSelfieCapture().upload("ACTIVITY", "DOCUMENT TYPE", (isSuccess, result) -> { if (isSuccess) //Upload is SUCCESS! });
```

#### Usage of ScanNFC

For scanning NFC, will need two override methods. onNewIntent and onResume methods is required. Usage is below.

```java

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

//Uploading NFC datas

```java 
Amani.sharedInstance().ScanNFC().upload(this,"DOCUMENT TYPE", (uploadNFCSuccess, resultNFC, errors) -> 
{ 
} ); // It must call if ScanNFC is success.
```

## CallBack Guideline
There is a CallBack as the return type of the start and upload methods. This CallBack usually gives you 2 parameters. You can find out what these parameters mean below.

- You can change the names of these parameters as you wish.

### Upload CallBack:
```java 
//The method in front of the upload() method may vary according to the document as follows.
Amani.sharedInstance().ScanNFC()/IDCapture()/Selfie().upload(this,"DOCUMENT TYPE", (uploadNFCSuccess, resultNFC, errors) -> 
{ } ); 
```
- uploadNFCSuccess (type: boolean): The first parameter of the upload method is always set in a boolean sense. Indicates that the upload was successful/failed as true/false.
- resultNFC (type: String): The second parameter of the upload method is the string of the message returned from the service after the upload process is completed.  The cases where this message returns "OK" are returned when the service accepts this document.
- errors (type: ArrayList): It contains errorMessages, errorCodes if exists.

### Start CallBack: 
The return CallBack of the start function contains one parameter, except NFC, and two in NFC. The meanings of these parameters are below.

```java 
//The method in front of the start() method may vary according to the document as follows.
Amani.sharedInstance().ScanNFC()/IDCapture()/Selfie().start(tag, getApplicationContext(),
"BIRTH DATE" , 
"EXPIRE DATE" , 
"DOCUMENT NUMBER", 
(bitmap, isSuccess) -> { }
```
- bitmap (type: Bitmap) : If this parameter is not "null", it means that the result of the previous function was successful.

- isSuccess (type: boolean) : Indicates whether the operation was successful or unsuccessful.

### NFC Exception: 
```java 
...NFC().start("firstParam","secondParam,"thirdParam",(bitmap, isSuccess, exception)) 
```
The exception string in the NFC function's return callback returns an error message. You can find these messages below.

#### NFC Exception Messages: 

| Exceptions | Possible Cases  |  
| ------- | --- | 
| "Tag was lost" | Situations such as moving the ID during scanning or interrupting the scanning. | 
| "Failed to connect" | In cases where the connection is disconnected or the ID is removed from the device for some reason during the initial scan startup. | 
| "Invalid key" | Using incorrect MRZ data or incorrect input states. | 
| "General exception" | Instant or general exceptions due to device or ID card. | 
| "Biometric photo not found" | Situations such as the inability to receive the biometric photo from the NFC chip due to identity or device. | 






