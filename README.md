Force layout
====


Forcelayout is an Android library for visualizing data.

## Demo

![image](https://raw.githubusercontent.com/kai0masanari/Forcelayout/master/art/image1.gif)

![image](https://raw.githubusercontent.com/kai0masanari/Forcelayout/master/art/image2.gif)

## Usage
ノードの名前(画像名)とリソースファイルが含まれたノードの情報と、ノード間のリンクの情報を読み込んで使用する

### 1. Gradle
```java
repositories {
    jcenter()
}

dependencies {
    compile 'jp.kai:forcelayout:1.0.1'
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
Forcelayout.with(this).nodesize(150).setnodes(nodes).setlinks(links).linkStrength(0.1).distance(300);
```

## Thanks
Inspired by D3.js.