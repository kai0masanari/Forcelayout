Force layout
====


Forcelayout is an Android library for visualizing data.

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
    compile 'jp.kai:forcelayout:1.0.2'
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
Inspired by D3.js.