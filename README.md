<div align="center">
    <img src="https://tokyo-opendata.s3.amazonaws.com/logo.png" height="200px" width="200px"/>
    <br />
    <h3>Tokyo OpenData for ChatGPT Plugin</h3>
    <br />
    <p><a href="https://catalog.data.metro.tokyo.lg.jp/dataset">東京都オープンデータカタログサイト</a>に掲載されているオープンデータを検索できるChatGPT Pluginです。</p>
</div>

## ChatGPT Plugin とは？

ChatGPT Plugin は、ChatGPT に新しい機能を追加したり、既存機能を強化したりするための拡張ツールです。  
詳しくは下記の公式ドキュメントをご確認ください。

-   [ChatGPT plugins](https://openai.com/blog/chatgpt-plugins)

## Tokyo OpenData for ChatGPT Plugin とは？

Tokyo OpenData for ChatGPT Plugin は、<a href="https://catalog.data.metro.tokyo.lg.jp/dataset">東京都オープンデータカタログサイト</a>に掲載されているオープンデータを ChatGPT から検索したり、データを取得したりする ChatGPT Plugin です。

本 ChatGPT Plugin を利用することで、ChatGPT と対話しながらオープンデータの検索ができるようになります。

## 開発に参加するためには？

Tokyo OpenData for ChatGPT Plugin の開発に興味を持っていただき、ありがとうございます。  
より良いプラグインの実現のため、機能改善やバグ修正の参加を募集しています。

皆さんが開発に参加するために、一連の開発の流れの例を記載します。

### 1. issue の作成

機能改善の要望やバグの報告があれば、まずは報告をお願いします。

### 2. リポジトリのフォーク

GitHub のリポジトリページで本プロジェクトをフォークしてください。

### 3. プロダクトコードの修正

作成した issue に関して、プロダクトコードに変更を加えましょう。

### 4. 変更したコードの push

変更したコードを push しましょう。  
ブランチ名は「feature/issue-X」のような命名規則としてください。

### 5. PR の作成

PR を作成しましょう。CODEOWNERS のメンバーがレビューします。

### 6. プルリクエストのレビュー

プルリクエストがレビューされ、問題がなければ本プロジェクトにマージされます。  
フィードバックや修正のリクエストがある場合は、それに応じて変更を加えてください。

## 開発準備

事前に Java のインストールをお願いします。

```
$ git clone https://github.com/<your_account>/tokyo-opendata-chatgpt-plugin.git
$ cd tokyo-opendata-chatgpt-plugin.git
$ ./gradlew clean bootJar
$ java -jar build/libs/tokyo-opendata-chatgpt-plugin.jar
```

## 参考資料

開発に入る前に以下の資料の確認をおすすめします。

1. ChatGPT Plugin の開発方法のドキュメント  
   ChatGPT Plugin の開発の上で必要な情報が記載されています。  
   [Introduction - OpenAI API](https://platform.openai.com/docs/plugins/introduction)

2. CKAN のドキュメント  
   オープンデータを取得する API の機能について記載されています。  
   [API guide — CKAN 2.8.12 documentation](https://docs.ckan.org/en/2.8/api/)
