# Robotronic #

Robotonic is a library for handling data driven Android apps. It simplifies common tasks like fetching data and images from the internet and caching the results.

## How does it work? ##

Each call you make for remote assets will return twice, immediately with the data from the cache and then again after the assets have been fetched from their remote sources. This allows you to show old data immediately for a snappy user experience, then update the screen with fresh data when it becomes available. Robotronic also handles the background threading of these long tasks to keep the user interface responsive.

## Why would I use it? ##

A huge percentage of Android apps pull data down from a feed, parse the contents and display the results. Considering how common this task is, Android doesn't make it overly easy to do. What's more, the large latency typical of 3G connections leaves the user staring at a blank screen for extended periods of time. Robotronic makes the first problem much simpler and the second non existent.

## How can I use it? ##

Robotronic attempts to simply get out of the way and let you worry about more important problems. Usage information can be found in the [wiki](https://github.com/drewschrauf/robotronic/wiki) and examples of use can be found in the source directory under `com.drewschrauf.example.robotronic`.