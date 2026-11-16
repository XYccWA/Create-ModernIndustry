# Create: Modern Industry

Create（机械动力）的附属模组，主题是「现代工业」：现代化产线、工业机壳与航空/物理配套设备。

- **Minecraft**: 1.21.1
- **加载器**: NeoForge 21.1.x（本工程基准 `21.1.234`）
- **前置**: Create 6.0.10+、Flywheel、Ponder、Registrate
- **软依赖**: Create: Aeronautics（Sable + Simulated + Aeronautics + Offroad）、JEI

> 当前状态：**基础骨架已完成并验证**。
> `gradlew build`（出 jar）、`gradlew runData`（生成资源）、`gradlew runClient`（启动到主菜单）
> 三条链路都已跑通：模组在 Create + 航空学 + JEI 全装的环境下正常加载，
> 注册表 / 配置 / 兼容探测 / 资源与配方数据都正常产出。
> 玩法内容（机器、配方链、Ponder 场景）在此之上继续加。

### 已验证

| 检查项 | 结果 |
| --- | --- |
| `gradlew build` | 通过，产物 `build/libs/create_modern_industry-0.1.0.jar` |
| `gradlew runData` | 通过，生成 blockstate / 模型 / 战利品表 / 配方 / 标签 / en_us / zh_cn |
| `gradlew runClient` | 通过，加载到主菜单无异常（Create 6.0.10 + Sable 2.0.5 + Simulated/Aeronautics/Offroad 1.3.1 + JEI 19.21.0.247） |
| 兼容探测日志 | 正常打印 6 个兼容模组版本 |

进游戏后请手动确认一次 `/cmi status`（需要进存档，命令会走一遍真实的 Sable 子层级查询）。

---

## 环境相关的两个坑（本机必须注意）

1. **`maven.neoforged.net` 连不上**：`build.gradle` 里必须先 `repositories.clear()`
   再声明 `neoforged.forgecdn.net` 镜像，否则 Gradle 会卡在连接上很久。
2. **`JAVA_HOME` 是 JDK 17，但工程要 Java 21**：`gradle.properties` 里用
   `org.gradle.java.installations.paths=D:/java21` 直接指到本机 JDK 21，
   避免 foojay 联网下载工具链。
3. 另外 `neoforge.mods.toml` 模板里**不能出现占位符字面量**（`$` + 花括号），
   模板引擎会把它当表达式求值而报错；中文注释依赖 `filteringCharset = 'UTF-8'`。

---

## 快速开始

```powershell
# 编译（会首次处理 Minecraft/NeoForge 产物，第一次较慢）
.\gradlew.bat build

# 启动开发客户端（已内置 Create + 航空学 + JEI）
.\gradlew.bat runClient

# 直接进指定存档，省掉菜单点击
.\gradlew.bat runClient -PquickPlaySingleplayer=测试世界

# 数据生成：方块模型/blockstate/战利品表/英文 lang/配方/中文 lang
.\gradlew.bat runData

# 开发服务端
.\gradlew.bat runServer
```

产物在 `build/libs/create_modern_industry-<version>.jar`。

---

## 目录结构

```
src/main/java/org/xyccwa/create_modern_industry/
├── CreateModernIndustry.java            主类：配置 / Registrate / 注册 / 兼容层 / 命令
├── CreateModernIndustryClient.java      仅客户端初始化（Ponder 插件注册）
├── CreateModernIndustryDataGenerator.java  datagen 总入口（runData）
├── index/                               注册表入口（只放「注册」，不放逻辑）
│   ├── CMIBlocks.java                   方块
│   ├── CMIItems.java                    物品
│   └── CMICreativeTabs.java             创造模式物品栏
├── config/CMIConfig.java                通用配置（common）
├── compat/                              所有软依赖代码都在这里
│   ├── CompatFlags.java                 纯 ModList 探测，任何环境都能安全调用
│   ├── sable/SableCompat.java           Sable 子层级查询（两层隔离 + try/catch）
│   ├── aeronautics/AeronauticsCompat.java  航空学套件探测与转发
│   ├── jei/CMIJeiPlugin.java            JEI 插件（缺 JEI 时类不会被加载）
│   └── ponder/CMIPonderPlugin.java      Ponder 教学场景插件（客户端）
├── command/CMICommands.java             /cmi status 调试命令
├── dataGen/                             手写的数据生成器
│   ├── CMIRecipeProvider.java           配方
│   └── CMIZhCnLangProvider.java         简体中文语言
├── mixin/                               Mixin 包（当前为空骨架）
└── util/CMITags.java                    标签定义（c:ingots/modern_steel）

src/main/templates/META-INF/neoforge.mods.toml   mod 元数据模板（${} 由 gradle 展开）
src/main/resources/create_modern_industry.mixins.json   Mixin 配置（空骨架）
src/main/resources/assets/create_modern_industry/...    贴图等静态资源
src/generated/resources/...                               runData 的产物（已纳入资源目录）
```

---

## 兼容性设计（重点）

三个软依赖共用同一套写法，**核心原则：判断在前，接触 API 在后**。

```java
// 1) 只问 ModList，不引用对方任何类
if (CompatFlags.isSableLoaded()) {
    // 2) 真正的 API 调用被关在独立类 / 独立包 / try-catch 里
    SableCompat.subLevelNameAt(level, pos);
}
```

| 模组 | 声明方式 | 代码位置 | 缺失时的行为 |
| --- | --- | --- | --- |
| Create | `required` + `ordering=AFTER` | 全局 | 直接拒绝启动（本来就是前置） |
| Sable / Simulated / Aeronautics / Offroad | `optional` + `AFTER` | `compat/sable`、`compat/aeronautics` | `/cmi status` 报告 `disabled`，功能降级但不崩溃 |
| JEI | `optional`（CLIENT）+ `AFTER` | `compat/jei` | 插件类不会被加载，物品信息页消失，其它正常 |

游戏内自查：

```
/cmi status
```

会打印本模组版本、各兼容模组版本、航空学套件是否齐全、Sable API 是否可用，
以及玩家当前是否站在某块航空载具（Sable 子层级）上。

编译期依赖的写法见 `build.gradle`：航空学四件套走 `localImplementation`
（只参与本工程编译与运行，不传递给下游），JEI 走 `compileOnly` + `localRuntime`。

---

## 加内容的标准流程

### 加一个方块

```java
// index/CMIBlocks.java
public static final BlockEntry<Block> MY_BLOCK = CreateModernIndustry.REGISTRATE
        .block("my_block", Block::new)
        .initialProperties(() -> Blocks.IRON_BLOCK)
        .blockstate(BlockStateGen.simpleCubeAll("my_block"))   // 贴图 assets/<id>/textures/block/my_block.png
        .simpleItem()                                          // 同时生成方块物品
        .register();
```

Registrate 会自动生成：blockstate、cube_all 模型、物品模型、掉落自身的战利品表、英文语言键。
贴图需要自己放：`src/main/resources/assets/create_modern_industry/textures/block/my_block.png`。

### 加一个物品

```java
public static final ItemEntry<Item> MY_ITEM = CreateModernIndustry.REGISTRATE
        .item("my_item", Item::new)      // 自动 item/generated 模型 + 英文名
        .register();
```

### 加配方

写进 `dataGen/CMIRecipeProvider#buildRecipes`，然后 `gradlew runData`。
产物落在 `src/generated/resources/data/create_modern_industry/recipe/`。

### 加中文翻译

写进 `dataGen/CMIZhCnLangProvider#addTranslations`，`runData` 生成 `zh_cn.json`。
（英文由 Registrate 自动补，不用手写。）

### 加 Mixin

1. 类放到 `org.xyccwa.create_modern_industry.mixin`（客户端专用放子包）；
2. 在 `create_modern_industry.mixins.json` 的 `mixins` / `client` 数组里登记；
3. 混入别人（Create / Sable / Aeronautics）的类时，建议单独开一个
   `"required": false` 的配置文件，这样对方改实现时我方只是静默失效而不是启动崩溃。

### 加 Ponder 教学场景

1. 客户端注册已完成（`CreateModernIndustryClient` → `PonderIndex.addPlugin`）；
2. 场景结构文件放 `assets/create_modern_industry/ponder/<scene>.nbt`；
3. 在 `compat/ponder/CMIPonderPlugin#registerScenes` 里 `helper.addStoryBoard(...)` 写剧本。

---

## 版本升级备忘

所有版本号集中在 `gradle.properties`，升级时按依赖顺序改：

1. `create_version` / `ponder_version` / `flywheel_version` / `registrate_version`
2. `sable_version` / `create_aeronautics_version`（航空学四件套共用同一个版本号）
3. `jei_version`
4. `neo_version`（`neoforge.mods.toml` 里的 `[21.1.228,)` 下限也一并确认）

升级后至少跑一次 `gradlew build` + `runClient`，再用 `/cmi status` 确认兼容层仍然识别到航空学。

## 已知约定

- 命名空间 `create_modern_industry`；Java 包 `org.xyccwa.create_modern_industry`。
- `content/` 放玩法逻辑，`index/` 只放注册入口，`compat/` 只放软依赖代码 —— 不要混。
- 材料标签统一挂 `c:` 命名空间（例：`c:ingots/modern_steel`）。
- 所有路径相关字符串用 `CreateModernIndustry.asResource(path)` 生成。

## 许可证

本项目基于 **GNU General Public License v3.0（GPL-3.0）** 发布，全文见根目录 [`LICENSE`](LICENSE)。

Copyright (C) 2026 XYccWA

- 依赖的 Create / Create: Aeronautics / JEI 等模组各自保留其原许可证，本项目仅以「运行时链接」的方式使用它们。
- GPL-3.0 要求再分发（含修改后再分发）时附带许可证全文并提供对应源码；构建脚本已把 `LICENSE` 一并打进 jar。
- 版本号与许可证写在 `gradle.properties` 的 `mod_license`，会展开进 `neoforge.mods.toml`。
