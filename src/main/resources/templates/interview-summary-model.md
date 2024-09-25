
[//]: # (
    官方文档：https://www.thymeleaf.org/doc/tutorials/3.1/usingthymeleaf.pdf
    1. markdown 支持 html 标签，markdown 渲染器不支持也无所谓，反正都要转成 html
    2. 部分 markdown 渲染器支持 html 标签里写 markdown 代码（例如 IDEA 的），但是在转化为 html 的时候。html 标签中的 markdown 代码并不会被识别到
    3. 图片 src 如果是相对路径的形式，在邮件里是看不到的，因为我们发邮件提供的是 html 代码，但邮件厂商或多或少都限制在渲染的时候请求资源的总大小
    4. 插入的文本如果存在模板引擎的相关语法的符号，并不会二次替换，可以认为这个模板的特殊语法的符号都是一起被替换的，没有顺序之分
)
<div style="text-align: center;">
    <img src="https://www.freeimg.cn/i/2024/08/13/66bb1f6b81c84.png" width="144px" height="144px" alt="AchoBeta Interview Summary"/>
    <br/>
    <h1>AchoBeta Interview Summary</h1>
</div>

---

## **Preamble**

Dear student <strong th:text="${studentId}"></strong>, thank you for taking the time to participate in our recruitment process. This is a summary of your interview.

Regardless of the outcome, we hope you find the experience valuable!

## **Interview Title**

<span th:text="${title}"></span>

## **Foundational Ability (0 - 5)**

[//]: # (纯文本插入)
[(${basis})]

## **Programming Ability (0 - 5)**

[(${coding})]

## **Thinking Ability (0 - 5)**

[(${thinking})]

## **Expressive Ability (0 - 5)**

[(${express})]

## **Interview Overall Review**

[//]: # (使用注入标签的方式进行语法屏蔽)
<span th:text="${evaluate}"></span>

## **Suggestions**

<span th:text="${suggest}"></span>

## **Playback**

<span th:text="${playback}"></span>

## **Remarks**

If you have any concerns or objections regarding the results of the interview, please feel free to provide us with your feedback in a timely manner.
