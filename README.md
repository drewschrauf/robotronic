# Robotronic #
Robotonic is a library for handling data driven Android apps. It makes common tasks like fetching data and images from the internet and caching the results simple. Each call you make for remote assets will return twice, immediately with the data from the cache and then again after the assets have been fetched from their remote sources. This allows you to show old data immediately for a snappy user experience, then update the screen with fresh data when it becomes available. Robotronic also handles the background threading of these long tasks to keep the user interface responsive.

## Usage ##
You can use Robotronic just by extending `RobotronicActivity` then fetching data by calling:
    getThreadHandler().makeDataDownloader(url, handler)

binary files by calling:
    getThreadHandler().makeBinaryDownload(url, handler)

or binding images directory to ImageViews by calling:
    getThreadHandler().makeImageDownloader(url, imageView)

Examples of usage can be found in the source directory under `com.drewschrauf.example.robotronic`.




