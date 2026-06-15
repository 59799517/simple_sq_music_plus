# AGENTS.md

## 项目规则

### 代码修改流程
**每次修改代码前，必须先执行 Git 备份：**

```bash
# 1. 修改前备份
git add .
git commit -m "备份：修改前"

# 2. 执行修改
# ... Codex 修改代码 ...

# 3. 检查修改
git diff

# 4. 如果满意，提交修改
git add .
git commit -m "Codex修改完成"

# 5. 如果不满意，回滚修改
git reset --hard HEAD~1
```

### 语言偏好
- 使用中文回答和思考
- 代码注释使用中文

### 项目信息
- Simple Music Server Plus v3.1.8
- Spring Boot 3.5.12 + Java 21 + MySQL + MyBatis-Plus
