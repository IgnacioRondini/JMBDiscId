This is a fork from dietmar-steiner/JMBDiscId aiming to publish it as a maven dependency, so we can ease its utilisation. I am doing it because I couldn't contact the former maintainer. 

The Library is released to MavenCentral! So you can now import it to use it in your projects. Check https://search.maven.org/artifact/com.axokoi/JMBDiscId

As a maven dependency, you can add it as:

<dependency>
  <groupId>com.axokoi</groupId>
  <artifactId>JMBDiscId</artifactId>
  <version>1.0.1</version>
</dependency>


You can find the original readme here:

# Original Licence

This library is released under LGPL 2.1

The library depends on the following other Open Source Projects:

Apache Commons Logging 1.1.1  url: http://commons.apache.org/logging/
JNA 3.4.0 url: https://github.com/twall/jna
libdiscid url: http://musicbrainz.org/doc/libdiscid

I thank all of this people and organisations for having helped to make this project possible.

A special thanks to Western Waves Community Radio, Bundoran, Ireland for sponsoring the project.

At the moment there is 1 problem as I only have a windows environment.
So I cannot test in either Unix.
If anyone uses this software on an Unix system please let me know, so I can update 
the status of being tested on these systems.

On a further note libdiscid for windows is only available for 32 bit, so if anyone can 
generate me a 64bit dll of libdiscid, I could test it against that and we would have Windows 64bit. 

The distribution does not include the libdiscid library. Please download it from MusicBrainz.

For usage please look at examples.main.java        
