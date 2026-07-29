# AsyncCall

Simple concurrent execution tools for Android in Java.

> **A note on history.** AsyncCall was built in 2017–2018 and used in production at ThinkRite,
> before Kotlin coroutines existed as a practical option for Android. At the time, the choice
> was between `AsyncTask` (awkward lifecycle handling, easy to leak contexts) and RxJava
> (powerful, but a steep learning curve for a team that just needed "run this off the main
> thread and hand me the result safely"). AsyncCall was the minimal middle path: a small,
> dependency-free, lifecycle-aware wrapper around `ExecutorService` with guaranteed
> main-thread result delivery.
>
> Today, Kotlin coroutines solve this problem better. This repository is preserved as-is
> for historical reference and is no longer actively developed.

AsyncCall will execute a task in a worker thread and deliver the result when done. The client
should provide an `Executor` and the corresponding listeners.

## Usage example

Use the `Builder` class as follows:

```java
new Builder<List<Post>>()
        .withExecutorService(executorService)
        // This is the task, which assumes the form of a Callable:
        .async(postRepository::getPosts)
        // The result is obtained here:
        .onResult(posts -> {
            // Note the result can be null.
            assert posts != null;

            // Update the UI.
            posts.stream()
                    .findFirst()
                    .ifPresent(post -> binding.textviewFirst.setText(post.getTitle()));
        })
        // Since this example is called from a Fragment, observe its lifecycle:
        .observe(this)
        // Any exception will be received here:
        .except(e -> Log.e(TAG, "onViewCreated: ", e))
        .start();
```

Calling `start` from the UI thread guarantees delivery on the UI thread. Calling it from any
other thread delivers the result in a worker thread. This applies to exceptions as well.

## Design goals

- **Minimal API surface** — one builder, three callbacks. Learnable in minutes.
- **No third-party dependencies** — plain `java.util.concurrent` underneath.
- **Lifecycle-aware** — results are not delivered to dead Fragments or Activities.
- **Predictable threading** — the calling thread determines the delivery thread.

## Add to project

[![](https://jitpack.io/v/eddiellopez/asynccall.svg)](https://jitpack.io/#eddiellopez/asynccall)

Add JitPack to your project repositories:

```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency:

```groovy
dependencies {
    implementation 'com.github.eddiellopez:AsyncCall:tag'
}
```

See the [Latest Release](https://github.com/eddiellopez/AsyncCall/releases/latest) for the tag.
