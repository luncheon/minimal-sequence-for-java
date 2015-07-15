# minimal-sequence

## はじめに

[minimal-sequence](https://github.com/luncheon/minimal-sequence-for-java/) は Java 5, 6, 7 のために最小限のシーケンス操作を提供します。<br>
[Retrolambda](https://github.com/orfjackal/retrolambda/) の利用を前提としています。

ただ、「最小限」のシーケンス操作とは「私にとっての最小限」です。<br>
『[ブロートウェアと80/20の神話](http://japanese.joelonsoftware.com/Articles/StrategyLetterIV.html)』のとおり、「機能限定版の軽量ソフトウェア」は結局のところ誰のニーズにも応えられないでしょう。

世の中には素敵なコレクションライブラリがたくさんあります。<br>
是非そちらを使ってみてください。

* [streamsupport](http://sourceforge.net/projects/streamsupport/)
* [Functional Java](http://www.functionaljava.org/)
* [GS Collections](https://github.com/goldmansachs/gs-collections/)


## 経緯

Java 8 未満で実装するという苦行を何度も繰り返していると、毎回同じようなシーケンス操作ライブラリを書く羽目になります。<br>
特に Android アプリ開発では、 Java のバージョンが限定されることに加えてメソッド数 64k 制限があるため、巨大なライブラリの導入は躊躇われます。<br>
また、ライブラリを導入する際にはライセンスの問題を避けて通れません。具体的に何をすれば使っていいのか分からないために導入を見送ることもあります。


## ライセンス

パブリックドメインとします。誰でも「ご自分の作ったものを扱うのと同じように」扱うことができます。<br>
変更したものや部分的に使用したものは、あなたのものになります。公開する場合は、あなたの名前の下で行って下さい。


## 内容

* [Sequence&lt;T&gt;](https://luncheon.github.io/minimal-sequence-for-java/javadoc/minimal/sequence/Sequence.html): Iterable&lt;T&gt; をラップして each, map, filter, takeWhile, any, groupBy などを提供します。
* [Maybe&lt;T&gt;](https://luncheon.github.io/minimal-sequence-for-java/javadoc/minimal/sequence/Maybe.html): 値が存在しない可能性のあるコンテナ (要素数 0 or 1 の Iterable) として each, map, filter などを提供します。

### コード例

```java
Sequence.of("123", "456", "789")        // <= Array or Iterable
        .map(Integer::valueOf)
        .prepend(22, 33, 44)
        .append(55, 66, 77)
        .filter(x -> x % 2 == 0)
        .each(System.out::println)      // 22 44 456 66
        .first()                        // => Maybe<Integer>(22)
        .each(System.out::println)      // 22
        .map(x -> x + 100)
        .match(
                () -> "empty",
                x -> "exists " + x
        )                               // => String("exists 122")
```


```java
// クイックソート
static Sequence<Integer> sort(Sequence<Integer> sequence) {
    return sequence.match(
            () -> sequence,
            (first, rest) -> Sequence.of(first)
                    .prepend(sort(rest.filter(x -> x <= first)))
                    .append(sort(rest.filter(x -> x > first)))
    );
}
```
