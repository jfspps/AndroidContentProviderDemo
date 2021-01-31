# AndroidContentProviderDemo

A demo of Content Resolver and pPovider classes in Android. This example extracts phone contacts and displays them in a TextView.

Note that the changes of Android permissions from API 23 (Marshmallow) and newer means that the 
implementation of contact extraction differs for API 22 (Lollipop) and older. Both cases are 
demonstrated in this project.

API 23 and above requires the app to request Dangerous Permissions when it is run and not when an app is 
installed. Such permissions can be revoked by the user.

This also means that the app behaviour when permissions are denied in addition to when it was granted.
The user's choice can be set as a default ("don't ask again") which might also affect app 
behaviour when the app restarts at some other point.