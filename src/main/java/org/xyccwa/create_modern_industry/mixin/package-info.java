/**
 * Mixin 包。
 *
 * <p>当前 {@code create_modern_industry.mixins.json} 是空骨架：还没有任何 mixin，
 * 但配置文件与包结构已经就位，后续加 mixin 时只需要：
 * <ol>
 *     <li>在 {@code create_modern_industry.mixins.json} 的 {@code mixins}（通用）或
 *         {@code client}（仅客户端）数组里写上类名（相对本包）</li>
 *     <li>类本身放在本包里，{@code client} 数组里的类必须只被客户端加载</li>
 * </ol>
 *
 * <p>注意：混入别人（Create / Sable / Aeronautics）的类时，目标方法可能随版本变化，
 * 建议单独拆一个 mixin 配置文件并声明 {@code "required": false}，
 * 这样对方改实现时我方只是静默失效，而不是开不了游戏。
 */
package org.xyccwa.create_modern_industry.mixin;
