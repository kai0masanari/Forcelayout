Force layout
====
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Forcelayout-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/4392)
[![License](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Download](https://api.bintray.com/packages/kai0masanari/maven/forcelayout/images/download.svg)](https://bintray.com/kai0masanari/maven/forcelayout/_latestVersion)

Forcelayout is an Android library for visualizing data. You can drawing graph with spring-like attractive forces.

## Demo

![image](https://raw.githubusercontent.com/kai0masanari/Forcelayout/master/art/image1.gif) ![image](https://raw.githubusercontent.com/kai0masanari/Forcelayout/master/art/image2.gif)

## Usage
Usage in kotlin

### 1. Gradle
```groovy
repositories {
    jcenter()
}

dependencies {
    compile 'jp.kai:forcelayout:1.1.0'
}
```

### 2. Definitions of  nodes
```kotlin
val nodes = ArrayList <Pair<String,Int>>()
nodes.add(Pair("nodeName1", R.drawable.example1))
nodes.add(Pair("nodeName2", R.drawable.example2))
nodes.add(Pair("nodeName3", R.drawable.example3))
```

or

```kotlin
val nodes: Nodes = Nodes()
nodes.add(NodePair("nodeName1", R.drawable.example1))
nodes.add(NodePair("nodeName2", R.drawable.example2))
nodes.add(NodePair("nodeName3", R.drawable.example3))
```

If you don't use image. You can just write node name.

```kotlin
val nodes: Array<String> = arrayOf( "nodeName1", "nodeName2", "nodeName3")
```

![image](https://raw.githubusercontent.com/kai0masanari/Forcelayout/master/art/image5.gif)

### 3. Definitions of links
```kotlin
val links = Arrays.asList("nodeName1-nodeName2", "nodeName1-nodeName3")
```

or

```kotlin
val links: Links = Links()
links.add(LinkPair("nodeName1", "nodeName2"))
links.add(LinkPair("nodeName1", "nodeName3"))
```

### 4. Set node and link style (optional)
```kotlin
val forcelayout = Forcelayout(applicationContext)

forcelayout.node()
    .size(100)
    .style(Color.argb(100,200,30,50))

forcelayout.link()
    .style(Color.argb(60,50,30,200), 5.0f)
```

### 5. Set nodes and links
You can set node's size and link-strength, and so on. Please use anko if you write in kotlin.

```kotlin
val forcelayout = Forcelayout(applicationContext) 

forcelayout.with(this)
	.size(200)
	.distance(200)
	.gravity(0.04)
	.friction(0.04)
	.nodes(nodes)
	.links(links)
	.start()
```

| Params  | Description |
| ------------- | ------------- |
| `size`  | To set image width.  |
| `distance`  | To set distance between each nodes.  |
| `gravity`  | To set gravitation.  |
| `friction`  | To set gravitation between pair of nodes.  |
| `nodes`  | To set nodes that contain node name and image.  |
| `links`  | To set pairs that contain node names.  |

## Thanks
Inspired by `force layout` in [D3.js](https://d3js.org/).

## License
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
