The coding standard follows the [Google Coding Standard](https://google.github.io/styleguide/javaguide.html) with the following ammendments:
1. No cargo cult. If you find yourself importing a lot of packages from the same library it makes better sense to use a wild card than list every single one. Use your own discretion.
```java
// UNDESIRABLE
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;

// PREFERRED
import java.util.*;
```
2. Indentation should be 4 spaces deep on each block, not 2.
3. Do not mix tabs and spaces.
4. You SHOULD wrap the line if it reaches 80 characters wide, you MUST wrap the line if it exceeds 100.
5. When line wrapping the continuation line should be indented 8 spaces deep.
6. Do not push redundant or erroneous code that has been commented out, unless you think it is important that it remain there. If you do, please leave a comment explaining why.
