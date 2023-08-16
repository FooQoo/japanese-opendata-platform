<div align="center">
    <img src="https://tokyo-opendata.s3.amazonaws.com/logo.png" height="200px" width="200px"/>
    <br />
    <h3>Tokyo OpenData for ChatGPT Plugin</h3>
    <br />
    <p><a href="https://catalog.data.metro.tokyo.lg.jp/dataset">東京都オープンデータカタログサイト</a>に掲載されているオープンデータを検索できるChatGPT Pluginです。</p>
</div>

## ChatGPT Pluginとは？
ChatGPT Pluginは、ChatGPTに新しい機能を追加したり、既存機能を強化したりするための拡張ツールです。  
詳しくは下記の公式ドキュメントをご確認ください。  
- [ChatGPT plugins](https://openai.com/blog/chatgpt-plugins)

## Tokyo OpenData for ChatGPT Pluginとは？
Tokyo OpenData for ChatGPT Pluginは、<a href="https://catalog.data.metro.tokyo.lg.jp/dataset">東京都オープンデータカタログサイト</a>に掲載されているオープンデータをChatGPTから検索したり、データを取得したりするChatGPT Pluginです。  

本ChatGPT Pluginを利用することで、ChatGPTと対話しながらオープンデータの検索ができるようになります。  

## 開発に参加するためには？
Tokyo OpenData for ChatGPT Pluginの開発に興味を持っていただき、ありがとうございます。  
より良いプラグインの実現のため、機能改善やバグ修正の参加を募集しています。  

皆さんが開発に参加するために、一連の開発の流れの例を記載します。  

### 1. issueの作成
機能改善の要望やバグの報告があれば、まずは報告をお願いします。  

### 2. プロダクトコードの修正
作成したissueに関して、プロダクトコードを加えましょう。

### 3. 変更したコードのpush
変更したコードをpushしましょう。  
ブランチ名は「feature/issue-X」のような命名規則としてください。  

### 4. PRの作成
PRを作成しましょう。CODEOWNERSのメンバーがレビューします。  

### 5. プルリクエストのレビュー
プルリクエストがレビューされ、問題がなければ本プロジェクトにマージされます。  
フィードバックや修正のリクエストがある場合は、それに応じて変更を加えてください。  

## 開発準備
事前にJavaのインストールをお願いします。
```
$ git clone https://github.com/FooQoo/tokyo-opendata-chatgpt-plugin.git
$ cd tokyo-opendata-chatgpt-plugin.git
$ ./gradlew clean bootJar
$ java -jar build/libs/tokyo-opendata-chatgpt-plugin.jar
```

## 参考資料
開発に入る前に以下の資料の確認をおすすめします。

1. ChatGPT Pluginの開発方法のドキュメント  
ChatGPT Pluginの開発の上で必要な情報が記載されています。  
[Introduction - OpenAI API](https://platform.openai.com/docs/plugins/introduction)

2. CKANのドキュメント  
オープンデータを取得するAPIの機能について記載されています。  
[API guide — CKAN 2.8.12 documentation](https://docs.ckan.org/en/2.8/api/)
