# Robotronic #
Robotonic is a library for handling data driven Android apps. It simplifies common tasks like fetching data and images from the internet and caching the results. 

Each call you make for remote assets will return twice, immediately with the data from the cache and then again after the assets have been fetched from their remote sources. This allows you to show old data immediately for a snappy user experience, then update the screen with fresh data when it becomes available. Robotronic also handles the background threading of these long tasks to keep the user interface responsive.

## Usage ##
Using Robotronic is simple, you can get started by extending `RobotronicActivity` then fetching data by calling:

    getThreadHandler().makeDataDownloader(url, handler)

binary files by calling:

    getThreadHandler().makeBinaryDownload(url, handler)

or binding images directory to ImageViews by calling:

    getThreadHandler().makeImageDownloader(url, imageView)

Examples of usage can be found in the source directory under `com.drewschrauf.example.robotronic`. More information about Robotronic can be found in the [wiki](https://github.com/drewschrauf/robotronic/wiki).




