Force layout
====
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Forcelayout-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/4392)
[![License](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Download](https://api.bintray.com/packages/kai0masanari/maven/forcelayout/images/download.svg)](https://bintray.com/kai0masanari/maven/forcelayout/_latestVersion)

Forcelayout is an Android library for visualizing data. You can drawing graph with spring-like attractive forces.

## Demo

![image](https://raw.githubusercontent.com/kai0masanari/Forcelayout/master/art/image2.gif)

![image](https://raw.githubusercontent.com/kai0masanari/Forcelayout/master/art/image3.gif)

## Usage

### 1. Gradle
```java
repositories {
    jcenter()
}

dependencies {
    compile 'jp.kai:forcelayout:1.0.3'
}
```

### 2. Definitions of  nodes
```java
HashMap<String, Integer> nodes = new HashMap<>();

nodes.put("labelname_1",R.drawable.example1);
nodes.put("labelname_2",R.drawable.example2);
```

### 3. Definitions of links
```java
List<String> links = new ArrayList<String>();

links.put("labelname_1-labelname_2");
```

### 4. Set nodes and links
You can set node's size and linkstrength, and so on.
```java
Forcelayout.with(this)
	.nodesize(200)
	.linkStrength(0.1)
	.distance(200)
	.gravity(0.04)
	.nodes(nodes)
	.links(links);
```

## Thanks
Inspired by `force layout` in [D3.js](https://d3js.org/).


##License
-------

    Copyright 2016 kai0masanari

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.