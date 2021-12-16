# AdventofCode-Java
Advent of Code - solutions in Java

# 说明
为了能够通过HTTP拿到用户输入数据，需要在 `aoc2021.AoC2021`中添加cookies
```java
static {
        COOKIES.put("_ga", "");
        COOKIES.put("_gid", "");
        COOKIES.put("_gat", "");
        COOKIES.put("session", "");
    }
```
以上值可从浏览器请求中获得，如下图：
![image](cookies.png)



# 有趣的站点
https://paiv.github.io/aoc2021/day/11/