Force layout
====

Overview
Force layout

## Example

![image](https://raw.githubusercontent.com/kai0masanari/Forcelayout/master/art/image.gif)

## Usage
ノードの名前(画像名)とリソースファイルが含まれたノードの情報と、ノード間のリンクの情報を読み込んで使用する

ノードの定義
HashMap<String, Integer> nodes = new HashMap<>();
nodes.put("ラベルネーム1",R.drawable.example1);
nodes.put("ラベルネーム2",R.drawable.example2);

リンクの定義
HashMap<String, String> links = new HashMap<>();
links.put("ラベルネーム1","ラベルネーム2");

Forcelayout.with(this).nodesize(150).setnodes(nodes).setlinks(links);

