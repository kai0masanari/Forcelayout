Force layout
====

Overview
Force layout

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

nodes.put("ラベルネーム1",R.drawable.example1);
nodes.put("ラベルネーム2",R.drawable.example2);
```


### 3. Definitions of links
```java
List<String> links = new ArrayList<String>();

links.put("ラベルネーム1-ラベルネーム2");
```

### 4. Set nodes and links
```java
Forcelayout.with(this).nodesize(150).setnodes(nodes).setlinks(links).linkStrength(0.1).distance(300);
```
