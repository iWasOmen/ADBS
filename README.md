#### This is a lightweight in-memory database system from coursework of the course Advanced Database System. 
It translates SQL queryies to relational algebra plans, evaluates and implements the most common relational operators, and outputs the correct query answers.

#### Explanation of how to extract join conditions from WHERE clasue:
The overall logic for extracting join conditions is that: according to the table name on both sides of the symbol, it is judged whether it is a select or a join expression, and the output is split into independent select and join expression lists in the ExtractWhere Class, and then the expressions of the same table and the same type are merged in the QueryPlan Class, and an AND expression is constructed.

Please see more details in ExtractWhere Class and QueryPlan Class, and also other Class. You may use @explanation to search some highlight comments.
