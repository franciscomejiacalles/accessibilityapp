The coding standard follows the [Google Coding Standard](https://google.github.io/styleguide/javaguide.html) with the following ammendments:
1. No cargo cult. If you find yourself importing more than 3 packages from the same library, it is better to use a wild card import than to list every single import.
```java
// BAD
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;

// GOOD
import java.util.*;
```
- Indentation should be 4 spaces deep on each block, not 2.
  - Do not mix tabs and spaces.
- You SHOULD wrap the line if it reaches 80 characters wide, you MUST wrap the line if it exceeds 100.
  - When line wrapping the continuation line should be indented 8 spaces deep.
