# AndroidContentProviderDemo

A demo of Content Resolver and pPovider classes in Android. This example extracts phone contacts and displays them in a TextView.

Note that the changes of Android permissions from API 23 (Marshmallow) and newer means that the 
implementation of contact extraction differs for API 22 (Lollipop) and older. Both cases are 
demonstrated in this project.

API 23 and above requires the app to request Dangerous Permissions when it is run and not when an app is 
installed. Such permissions can be revoked by the user.

This also means that the app behaviour when permissions are denied in addition to when it was granted.
If the user denies permission then the app will, when restarted, ask the user again for permission. 
 At this point, a "don't ask again" option appears and continues to appear until the user grants 
 permission. If a user grants permissions then they would not normally be asked again and thus
  the app should retain the choice on restart. The granted permission can be revoked from the 
  Android Settings menu.