#QUnit xBrowser Maven Plugin
Maven Plugin for running javascript unit tests using qunit on multiple web browsers. The cross browser testing is provided by Selenium. 

## Getting Started

Final releases will be published in [Maven Central](http://search.maven.org/), so no additional repositories are required. Until then you can grab 
development SNAPSHOT releases by adding this repository to your POM:

```xml
<repository>
    <id>sonatype-snapshots</id>
    <url>https://oss.sonatype.org/content/repositories/snapshots</url>
</repository>
```

Now you can use the plugin by adding the following in your POM:

```xml
<plugin>
    <groupId>com.storyiq.oss</groupId>
    <artifactId>qunit-xbrowser-maven-plugin</artifactId>
    <version>0.2-SNAPSHOT</version>             
</plugin>
```

## License and Copyright

Please read [LICENSE] (https://github.com/StoryIQ/qunit-xBrowser-maven-plugin/LICENSE) for licensing and copyright details

## Hacking

Contributions are most welcome, you will need a JDK, Maven and Git to start working with the code. Please set your IDE/text editor to use:

### Submodules ###

The repository uses submodules and they are required for the tests in the build to successfully work.

Following are the steps to get the submodules:

1. `git clone https://github.com/StoryIQ/qunit-xBrowser-maven-plugin.git`
2. `git submodule init`
3. `git submodule update`

Or:

1. `git clone https://github.com/StoryIQ/qunit-xBrowser-maven-plugin.git`
2. `git submodule update --init`

### Style Guidelines ###

* UTF-8 file encoding
* LF (UNIX) line endings
* 4 Space indent (no tabs)
  * Java
  * XML

Pull requests where an entire file has insignificant whitespace changes will stop us from seeing your changes.