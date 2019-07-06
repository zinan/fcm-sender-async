# fcm-sender-async

A fork of gcm-sender-async from Open Whisper Systems https://github.com/signalapp/gcm-sender-async GCM upgraded to FCM


## Installing

Add to your pom:

```
<dependency>
  <groupId>tr.com.sinanturgut</groupId>
  <artifactId>fcm-sender-async</artifactId>
  <version>0.1.5</version>
</dependency>
```

## Using

```
String apiKey = "<myFcmApiKey>";
Sender sender = new Sender(apiKey);

ListenableFuture<Result> future = sender.send(Message.newBuilder()
                                                     .withDestination("<registration_id>")
                                                     .withDataPart("message", "hello world!");

Futures.addCallback(future, new FutureCallback<Result>() {
  @Override
  public void onSuccess(Result result) {
    if (result.isSuccess()) {
      // Maybe do something with result.getMessageId()
    } else {
      // Maybe do something with result.getError(), or check result.isUnregistered, etc..
    }
  }

  @Override
  public void onFailure(Throwable throwable) {
    // Handle network failure or server 500
  }
}
```

License
---------------------

Copyright 2015 Open Whisper Systems

Licensed under the AGPLv3: https://www.gnu.org/licenses/agpl-3.0.html
