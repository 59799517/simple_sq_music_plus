import{y as ue,T as Bt,E as Ne,bk as bn,aX as pn,B as E,r as _,aG as Ze,ae as ut,K as d,a4 as mn,V as Ct,a2 as Ge,P as gt,be as yn,bl as wt,H as _t,q as K,x as L,t as ce,z as Le,C as be,A as bt,bm as Cn,D as De,a5 as Et,bn as wn,N as Q,a6 as Te,a0 as $t,Z as ee,a9 as Re,aA as At,aI as pt,ac as ie,bo as xn,I as Oe,ab as ft,L as Xe,$ as Sn,a_ as kn,J as Rn,ak as ze,ay as Pn,bp as Tn,bq as X,a7 as On,n as zn,ad as ve,O as xt,br as Fn,G as In,bs as Mn,F as Bn,aE as _n,aB as En,af as $n,ag as An,ba as St,bt as Nn,bc as Ln,bb as Dn}from"./index-b35506a1.js";import{u as kt}from"./use-merged-state-594d7f1d.js";import{a as Nt,c as Ye,f as Hn,i as mt,h as Kn,N as Vn,u as Wn,b as ht,V as jn,d as Un,e as Gn}from"./Tooltip-09a39f06.js";import{u as Lt,N as qn}from"./Input-8f15ebc0.js";function Ae(e,n){let{target:t}=e;for(;t;){if(t.dataset&&t.dataset[n]!==void 0)return!0;t=t.parentElement}return!1}function Rt(e){switch(typeof e){case"string":return e||void 0;case"number":return String(e);default:return}}function rt(e){const n=e.filter(t=>t!==void 0);if(n.length!==0)return n.length===1?n[0]:t=>{e.forEach(r=>{r&&r(t)})}}function Pt(e){return e&-e}class Zn{constructor(n,t){this.l=n,this.min=t;const r=new Array(n+1);for(let o=0;o<n+1;++o)r[o]=0;this.ft=r}add(n,t){if(t===0)return;const{l:r,ft:o}=this;for(n+=1;n<=r;)o[n]+=t,n+=Pt(n)}get(n){return this.sum(n+1)-this.sum(n)}sum(n){if(n===void 0&&(n=this.l),n<=0)return 0;const{ft:t,min:r,l:o}=this;if(n>o)throw new Error("[FinweckTree.sum]: `i` is larger than length.");let s=n*r;for(;n>0;)s+=t[n],n-=Pt(n);return s}getBound(n){let t=0,r=this.l;for(;r>t;){const o=Math.floor((t+r)/2),s=this.sum(o);if(s>n){r=o;continue}else if(s<n){if(t===o)return this.sum(t+1)<=n?t+1:o;t=o}else return o}return t}}let qe;function Yn(){return typeof document>"u"?!1:(qe===void 0&&("matchMedia"in window?qe=window.matchMedia("(pointer:coarse)").matches:qe=!1),qe)}let lt;function Tt(){return typeof document>"u"?1:(lt===void 0&&(lt="chrome"in window?window.devicePixelRatio:1),lt)}const Xn=Ye(".v-vl",{maxHeight:"inherit",height:"100%",overflow:"auto",minWidth:"1px"},[Ye("&:not(.v-vl--show-scrollbar)",{scrollbarWidth:"none"},[Ye("&::-webkit-scrollbar, &::-webkit-scrollbar-track-piece, &::-webkit-scrollbar-thumb",{width:0,height:0,display:"none"})])]),Jn=ue({name:"VirtualList",inheritAttrs:!1,props:{showScrollbar:{type:Boolean,default:!0},items:{type:Array,default:()=>[]},itemSize:{type:Number,required:!0},itemResizable:Boolean,itemsStyle:[String,Object],visibleItemsTag:{type:[String,Object],default:"div"},visibleItemsProps:Object,ignoreItemResize:Boolean,onScroll:Function,onWheel:Function,onResize:Function,defaultScrollKey:[Number,String],defaultScrollIndex:Number,keyField:{type:String,default:"key"},paddingTop:{type:[Number,String],default:0},paddingBottom:{type:[Number,String],default:0}},setup(e){const n=Bt();Xn.mount({id:"vueuc/virtual-list",head:!0,anchorMetaName:Nt,ssr:n}),Ne(()=>{const{defaultScrollIndex:w,defaultScrollKey:R}=e;w!=null?g({index:w}):R!=null&&g({key:R})});let t=!1,r=!1;bn(()=>{if(t=!1,!r){r=!0;return}g({top:v.value,left:c})}),pn(()=>{t=!0,r||(r=!0)});const o=E(()=>{const w=new Map,{keyField:R}=e;return e.items.forEach((P,W)=>{w.set(P[R],W)}),w}),s=_(null),a=_(void 0),i=new Map,h=E(()=>{const{items:w,itemSize:R,keyField:P}=e,W=new Zn(w.length,R);return w.forEach((G,q)=>{const Z=G[P],Y=i.get(Z);Y!==void 0&&W.add(q,Y)}),W}),b=_(0);let c=0;const v=_(0),S=Ze(()=>Math.max(h.value.getBound(v.value-ut(e.paddingTop))-1,0)),y=E(()=>{const{value:w}=a;if(w===void 0)return[];const{items:R,itemSize:P}=e,W=S.value,G=Math.min(W+Math.ceil(w/P+1),R.length-1),q=[];for(let Z=W;Z<=G;++Z)q.push(R[Z]);return q}),g=(w,R)=>{if(typeof w=="number"){F(w,R,"auto");return}const{left:P,top:W,index:G,key:q,position:Z,behavior:Y,debounce:le=!0}=w;if(P!==void 0||W!==void 0)F(P,W,Y);else if(G!==void 0)z(G,Y,le);else if(q!==void 0){const te=o.value.get(q);te!==void 0&&z(te,Y,le)}else Z==="bottom"?F(0,Number.MAX_SAFE_INTEGER,Y):Z==="top"&&F(0,0,Y)};let T,M=null;function z(w,R,P){const{value:W}=h,G=W.sum(w)+ut(e.paddingTop);if(!P)s.value.scrollTo({left:0,top:G,behavior:R});else{T=w,M!==null&&window.clearTimeout(M),M=window.setTimeout(()=>{T=void 0,M=null},16);const{scrollTop:q,offsetHeight:Z}=s.value;if(G>q){const Y=W.get(w);G+Y<=q+Z||s.value.scrollTo({left:0,top:G+Y-Z,behavior:R})}else s.value.scrollTo({left:0,top:G,behavior:R})}}function F(w,R,P){s.value.scrollTo({left:w,top:R,behavior:P})}function O(w,R){var P,W,G;if(t||e.ignoreItemResize||B(R.target))return;const{value:q}=h,Z=o.value.get(w),Y=q.get(Z),le=(G=(W=(P=R.borderBoxSize)===null||P===void 0?void 0:P[0])===null||W===void 0?void 0:W.blockSize)!==null&&G!==void 0?G:R.contentRect.height;if(le===Y)return;le-e.itemSize===0?i.delete(w):i.set(w,le-e.itemSize);const re=le-Y;if(re===0)return;q.add(Z,re);const u=s.value;if(u!=null){if(T===void 0){const x=q.sum(Z);u.scrollTop>x&&u.scrollBy(0,re)}else if(Z<T)u.scrollBy(0,re);else if(Z===T){const x=q.sum(Z);le+x>u.scrollTop+u.offsetHeight&&u.scrollBy(0,re)}U()}b.value++}const p=!Yn();let m=!1;function $(w){var R;(R=e.onScroll)===null||R===void 0||R.call(e,w),(!p||!m)&&U()}function A(w){var R;if((R=e.onWheel)===null||R===void 0||R.call(e,w),p){const P=s.value;if(P!=null){if(w.deltaX===0&&(P.scrollTop===0&&w.deltaY<=0||P.scrollTop+P.offsetHeight>=P.scrollHeight&&w.deltaY>=0))return;w.preventDefault(),P.scrollTop+=w.deltaY/Tt(),P.scrollLeft+=w.deltaX/Tt(),U(),m=!0,Hn(()=>{m=!1})}}}function j(w){if(t||B(w.target)||w.contentRect.height===a.value)return;a.value=w.contentRect.height;const{onResize:R}=e;R!==void 0&&R(w)}function U(){const{value:w}=s;w!=null&&(v.value=w.scrollTop,c=w.scrollLeft)}function B(w){let R=w;for(;R!==null;){if(R.style.display==="none")return!0;R=R.parentElement}return!1}return{listHeight:a,listStyle:{overflow:"auto"},keyToIndex:o,itemsStyle:E(()=>{const{itemResizable:w}=e,R=Ge(h.value.sum());return b.value,[e.itemsStyle,{boxSizing:"content-box",height:w?"":R,minHeight:w?R:"",paddingTop:Ge(e.paddingTop),paddingBottom:Ge(e.paddingBottom)}]}),visibleItemsStyle:E(()=>(b.value,{transform:`translateY(${Ge(h.value.sum(S.value))})`})),viewportItems:y,listElRef:s,itemsElRef:_(null),scrollTo:g,handleListResize:j,handleListScroll:$,handleListWheel:A,handleItemResize:O}},render(){const{itemResizable:e,keyField:n,keyToIndex:t,visibleItemsTag:r}=this;return d(Ct,{onResize:this.handleListResize},{default:()=>{var o,s;return d("div",mn(this.$attrs,{class:["v-vl",this.showScrollbar&&"v-vl--show-scrollbar"],onScroll:this.handleListScroll,onWheel:this.handleListWheel,ref:"listElRef"}),[this.items.length!==0?d("div",{ref:"itemsElRef",class:"v-vl-items",style:this.itemsStyle},[d(r,Object.assign({class:"v-vl-visible-items",style:this.visibleItemsStyle},this.visibleItemsProps),{default:()=>this.viewportItems.map(a=>{const i=a[n],h=t.get(i),b=this.$slots.default({item:a,index:h})[0];return e?d(Ct,{key:i,onResize:c=>this.handleItemResize(i,c)},{default:()=>b}):(b.key=i,b)})})]):(s=(o=this.$slots).empty)===null||s===void 0?void 0:s.call(o)])}})}}),me="v-hidden",Qn=Ye("[v-hidden]",{display:"none!important"}),Ot=ue({name:"Overflow",props:{getCounter:Function,getTail:Function,updateCounter:Function,onUpdateCount:Function,onUpdateOverflow:Function},setup(e,{slots:n}){const t=_(null),r=_(null);function o(a){const{value:i}=t,{getCounter:h,getTail:b}=e;let c;if(h!==void 0?c=h():c=r.value,!i||!c)return;c.hasAttribute(me)&&c.removeAttribute(me);const{children:v}=i;if(a.showAllItemsBeforeCalculate)for(const O of v)O.hasAttribute(me)&&O.removeAttribute(me);const S=i.offsetWidth,y=[],g=n.tail?b==null?void 0:b():null;let T=g?g.offsetWidth:0,M=!1;const z=i.children.length-(n.tail?1:0);for(let O=0;O<z-1;++O){if(O<0)continue;const p=v[O];if(M){p.hasAttribute(me)||p.setAttribute(me,"");continue}else p.hasAttribute(me)&&p.removeAttribute(me);const m=p.offsetWidth;if(T+=m,y[O]=m,T>S){const{updateCounter:$}=e;for(let A=O;A>=0;--A){const j=z-1-A;$!==void 0?$(j):c.textContent=`${j}`;const U=c.offsetWidth;if(T-=y[A],T+U<=S||A===0){M=!0,O=A-1,g&&(O===-1?(g.style.maxWidth=`${S-U}px`,g.style.boxSizing="border-box"):g.style.maxWidth="");const{onUpdateCount:B}=e;B&&B(j);break}}}}const{onUpdateOverflow:F}=e;M?F!==void 0&&F(!0):(F!==void 0&&F(!1),c.setAttribute(me,""))}const s=Bt();return Qn.mount({id:"vueuc/overflow",head:!0,anchorMetaName:Nt,ssr:s}),Ne(()=>o({showAllItemsBeforeCalculate:!1})),{selfRef:t,counterRef:r,sync:o}},render(){const{$slots:e}=this;return gt(()=>this.sync({showAllItemsBeforeCalculate:!1})),d("div",{class:"v-overflow",ref:"selfRef"},[yn(e,"default"),e.counter?e.counter():d("span",{style:{display:"inline-block"},ref:"counterRef"}),e.tail?e.tail():null])}});function Dt(e,n){n&&(Ne(()=>{const{value:t}=e;t&&wt.registerHandler(t,n)}),_t(()=>{const{value:t}=e;t&&wt.unregisterHandler(t)}))}const eo=ue({name:"Checkmark",render(){return d("svg",{xmlns:"http://www.w3.org/2000/svg",viewBox:"0 0 16 16"},d("g",{fill:"none"},d("path",{d:"M14.046 3.486a.75.75 0 0 1-.032 1.06l-7.93 7.474a.85.85 0 0 1-1.188-.022l-2.68-2.72a.75.75 0 1 1 1.068-1.053l2.234 2.267l7.468-7.038a.75.75 0 0 1 1.06.032z",fill:"currentColor"})))}}),to=ue({name:"Empty",render(){return d("svg",{viewBox:"0 0 28 28",fill:"none",xmlns:"http://www.w3.org/2000/svg"},d("path",{d:"M26 7.5C26 11.0899 23.0899 14 19.5 14C15.9101 14 13 11.0899 13 7.5C13 3.91015 15.9101 1 19.5 1C23.0899 1 26 3.91015 26 7.5ZM16.8536 4.14645C16.6583 3.95118 16.3417 3.95118 16.1464 4.14645C15.9512 4.34171 15.9512 4.65829 16.1464 4.85355L18.7929 7.5L16.1464 10.1464C15.9512 10.3417 15.9512 10.6583 16.1464 10.8536C16.3417 11.0488 16.6583 11.0488 16.8536 10.8536L19.5 8.20711L22.1464 10.8536C22.3417 11.0488 22.6583 11.0488 22.8536 10.8536C23.0488 10.6583 23.0488 10.3417 22.8536 10.1464L20.2071 7.5L22.8536 4.85355C23.0488 4.65829 23.0488 4.34171 22.8536 4.14645C22.6583 3.95118 22.3417 3.95118 22.1464 4.14645L19.5 6.79289L16.8536 4.14645Z",fill:"currentColor"}),d("path",{d:"M25 22.75V12.5991C24.5572 13.0765 24.053 13.4961 23.5 13.8454V16H17.5L17.3982 16.0068C17.0322 16.0565 16.75 16.3703 16.75 16.75C16.75 18.2688 15.5188 19.5 14 19.5C12.4812 19.5 11.25 18.2688 11.25 16.75L11.2432 16.6482C11.1935 16.2822 10.8797 16 10.5 16H4.5V7.25C4.5 6.2835 5.2835 5.5 6.25 5.5H12.2696C12.4146 4.97463 12.6153 4.47237 12.865 4H6.25C4.45507 4 3 5.45507 3 7.25V22.75C3 24.5449 4.45507 26 6.25 26H21.75C23.5449 26 25 24.5449 25 22.75ZM4.5 22.75V17.5H9.81597L9.85751 17.7041C10.2905 19.5919 11.9808 21 14 21L14.215 20.9947C16.2095 20.8953 17.842 19.4209 18.184 17.5H23.5V22.75C23.5 23.7165 22.7165 24.5 21.75 24.5H6.25C5.2835 24.5 4.5 23.7165 4.5 22.75Z",fill:"currentColor"}))}}),no=ue({props:{onFocus:Function,onBlur:Function},setup(e){return()=>d("div",{style:"width: 0; height: 0",tabindex:0,onFocus:e.onFocus,onBlur:e.onBlur})}});function zt(e){return Array.isArray(e)?e:[e]}const vt={STOP:"STOP"};function Ht(e,n){const t=n(e);e.children!==void 0&&t!==vt.STOP&&e.children.forEach(r=>Ht(r,n))}function oo(e,n={}){const{preserveGroup:t=!1}=n,r=[],o=t?a=>{a.isLeaf||(r.push(a.key),s(a.children))}:a=>{a.isLeaf||(a.isGroup||r.push(a.key),s(a.children))};function s(a){a.forEach(o)}return s(e),r}function ro(e,n){const{isLeaf:t}=e;return t!==void 0?t:!n(e)}function lo(e){return e.children}function io(e){return e.key}function ao(){return!1}function so(e,n){const{isLeaf:t}=e;return!(t===!1&&!Array.isArray(n(e)))}function co(e){return e.disabled===!0}function uo(e,n){return e.isLeaf===!1&&!Array.isArray(n(e))}function it(e){var n;return e==null?[]:Array.isArray(e)?e:(n=e.checkedKeys)!==null&&n!==void 0?n:[]}function at(e){var n;return e==null||Array.isArray(e)?[]:(n=e.indeterminateKeys)!==null&&n!==void 0?n:[]}function fo(e,n){const t=new Set(e);return n.forEach(r=>{t.has(r)||t.add(r)}),Array.from(t)}function ho(e,n){const t=new Set(e);return n.forEach(r=>{t.has(r)&&t.delete(r)}),Array.from(t)}function vo(e){return(e==null?void 0:e.type)==="group"}function go(e){const n=new Map;return e.forEach((t,r)=>{n.set(t.key,r)}),t=>{var r;return(r=n.get(t))!==null&&r!==void 0?r:null}}class bo extends Error{constructor(){super(),this.message="SubtreeNotLoadedError: checking a subtree whose required nodes are not fully loaded."}}function po(e,n,t,r){return Je(n.concat(e),t,r,!1)}function mo(e,n){const t=new Set;return e.forEach(r=>{const o=n.treeNodeMap.get(r);if(o!==void 0){let s=o.parent;for(;s!==null&&!(s.disabled||t.has(s.key));)t.add(s.key),s=s.parent}}),t}function yo(e,n,t,r){const o=Je(n,t,r,!1),s=Je(e,t,r,!0),a=mo(e,t),i=[];return o.forEach(h=>{(s.has(h)||a.has(h))&&i.push(h)}),i.forEach(h=>o.delete(h)),o}function st(e,n){const{checkedKeys:t,keysToCheck:r,keysToUncheck:o,indeterminateKeys:s,cascade:a,leafOnly:i,checkStrategy:h,allowNotLoaded:b}=e;if(!a)return r!==void 0?{checkedKeys:fo(t,r),indeterminateKeys:Array.from(s)}:o!==void 0?{checkedKeys:ho(t,o),indeterminateKeys:Array.from(s)}:{checkedKeys:Array.from(t),indeterminateKeys:Array.from(s)};const{levelTreeNodeMap:c}=n;let v;o!==void 0?v=yo(o,t,n,b):r!==void 0?v=po(r,t,n,b):v=Je(t,n,b,!1);const S=h==="parent",y=h==="child"||i,g=v,T=new Set,M=Math.max.apply(null,Array.from(c.keys()));for(let z=M;z>=0;z-=1){const F=z===0,O=c.get(z);for(const p of O){if(p.isLeaf)continue;const{key:m,shallowLoaded:$}=p;if(y&&$&&p.children.forEach(B=>{!B.disabled&&!B.isLeaf&&B.shallowLoaded&&g.has(B.key)&&g.delete(B.key)}),p.disabled||!$)continue;let A=!0,j=!1,U=!0;for(const B of p.children){const w=B.key;if(!B.disabled){if(U&&(U=!1),g.has(w))j=!0;else if(T.has(w)){j=!0,A=!1;break}else if(A=!1,j)break}}A&&!U?(S&&p.children.forEach(B=>{!B.disabled&&g.has(B.key)&&g.delete(B.key)}),g.add(m)):j&&T.add(m),F&&y&&g.has(m)&&g.delete(m)}}return{checkedKeys:Array.from(g),indeterminateKeys:Array.from(T)}}function Je(e,n,t,r){const{treeNodeMap:o,getChildren:s}=n,a=new Set,i=new Set(e);return e.forEach(h=>{const b=o.get(h);b!==void 0&&Ht(b,c=>{if(c.disabled)return vt.STOP;const{key:v}=c;if(!a.has(v)&&(a.add(v),i.add(v),uo(c.rawNode,s))){if(r)return vt.STOP;if(!t)throw new bo}})}),i}function Co(e,{includeGroup:n=!1,includeSelf:t=!0},r){var o;const s=r.treeNodeMap;let a=e==null?null:(o=s.get(e))!==null&&o!==void 0?o:null;const i={keyPath:[],treeNodePath:[],treeNode:a};if(a!=null&&a.ignored)return i.treeNode=null,i;for(;a;)!a.ignored&&(n||!a.isGroup)&&i.treeNodePath.push(a),a=a.parent;return i.treeNodePath.reverse(),t||i.treeNodePath.pop(),i.keyPath=i.treeNodePath.map(h=>h.key),i}function wo(e){if(e.length===0)return null;const n=e[0];return n.isGroup||n.ignored||n.disabled?n.getNext():n}function xo(e,n){const t=e.siblings,r=t.length,{index:o}=e;return n?t[(o+1)%r]:o===t.length-1?null:t[o+1]}function Ft(e,n,{loop:t=!1,includeDisabled:r=!1}={}){const o=n==="prev"?So:xo,s={reverse:n==="prev"};let a=!1,i=null;function h(b){if(b!==null){if(b===e){if(!a)a=!0;else if(!e.disabled&&!e.isGroup){i=e;return}}else if((!b.disabled||r)&&!b.ignored&&!b.isGroup){i=b;return}if(b.isGroup){const c=yt(b,s);c!==null?i=c:h(o(b,t))}else{const c=o(b,!1);if(c!==null)h(c);else{const v=ko(b);v!=null&&v.isGroup?h(o(v,t)):t&&h(o(b,!0))}}}}return h(e),i}function So(e,n){const t=e.siblings,r=t.length,{index:o}=e;return n?t[(o-1+r)%r]:o===0?null:t[o-1]}function ko(e){return e.parent}function yt(e,n={}){const{reverse:t=!1}=n,{children:r}=e;if(r){const{length:o}=r,s=t?o-1:0,a=t?-1:o,i=t?-1:1;for(let h=s;h!==a;h+=i){const b=r[h];if(!b.disabled&&!b.ignored)if(b.isGroup){const c=yt(b,n);if(c!==null)return c}else return b}}return null}const Ro={getChild(){return this.ignored?null:yt(this)},getParent(){const{parent:e}=this;return e!=null&&e.isGroup?e.getParent():e},getNext(e={}){return Ft(this,"next",e)},getPrev(e={}){return Ft(this,"prev",e)}};function Po(e,n){const t=n?new Set(n):void 0,r=[];function o(s){s.forEach(a=>{r.push(a),!(a.isLeaf||!a.children||a.ignored)&&(a.isGroup||t===void 0||t.has(a.key))&&o(a.children)})}return o(e),r}function To(e,n){const t=e.key;for(;n;){if(n.key===t)return!0;n=n.parent}return!1}function Kt(e,n,t,r,o,s=null,a=0){const i=[];return e.forEach((h,b)=>{var c;const v=Object.create(r);if(v.rawNode=h,v.siblings=i,v.level=a,v.index=b,v.isFirstChild=b===0,v.isLastChild=b+1===e.length,v.parent=s,!v.ignored){const S=o(h);Array.isArray(S)&&(v.children=Kt(S,n,t,r,o,v,a+1))}i.push(v),n.set(v.key,v),t.has(a)||t.set(a,[]),(c=t.get(a))===null||c===void 0||c.push(v)}),i}function Oo(e,n={}){var t;const r=new Map,o=new Map,{getDisabled:s=co,getIgnored:a=ao,getIsGroup:i=vo,getKey:h=io}=n,b=(t=n.getChildren)!==null&&t!==void 0?t:lo,c=n.ignoreEmptyChildren?p=>{const m=b(p);return Array.isArray(m)?m.length?m:null:m}:b,v=Object.assign({get key(){return h(this.rawNode)},get disabled(){return s(this.rawNode)},get isGroup(){return i(this.rawNode)},get isLeaf(){return ro(this.rawNode,c)},get shallowLoaded(){return so(this.rawNode,c)},get ignored(){return a(this.rawNode)},contains(p){return To(this,p)}},Ro),S=Kt(e,r,o,v,c);function y(p){if(p==null)return null;const m=r.get(p);return m&&!m.isGroup&&!m.ignored?m:null}function g(p){if(p==null)return null;const m=r.get(p);return m&&!m.ignored?m:null}function T(p,m){const $=g(p);return $?$.getPrev(m):null}function M(p,m){const $=g(p);return $?$.getNext(m):null}function z(p){const m=g(p);return m?m.getParent():null}function F(p){const m=g(p);return m?m.getChild():null}const O={treeNodes:S,treeNodeMap:r,levelTreeNodeMap:o,maxLevel:Math.max(...o.keys()),getChildren:c,getFlattenedNodes(p){return Po(S,p)},getNode:y,getPrev:T,getNext:M,getParent:z,getChild:F,getFirstAvailableNode(){return wo(S)},getPath(p,m={}){return Co(p,m,O)},getCheckedKeys(p,m={}){const{cascade:$=!0,leafOnly:A=!1,checkStrategy:j="all",allowNotLoaded:U=!1}=m;return st({checkedKeys:it(p),indeterminateKeys:at(p),cascade:$,leafOnly:A,checkStrategy:j,allowNotLoaded:U},O)},check(p,m,$={}){const{cascade:A=!0,leafOnly:j=!1,checkStrategy:U="all",allowNotLoaded:B=!1}=$;return st({checkedKeys:it(m),indeterminateKeys:at(m),keysToCheck:p==null?[]:zt(p),cascade:A,leafOnly:j,checkStrategy:U,allowNotLoaded:B},O)},uncheck(p,m,$={}){const{cascade:A=!0,leafOnly:j=!1,checkStrategy:U="all",allowNotLoaded:B=!1}=$;return st({checkedKeys:it(m),indeterminateKeys:at(m),keysToUncheck:p==null?[]:zt(p),cascade:A,leafOnly:j,checkStrategy:U,allowNotLoaded:B},O)},getNonLeafKeys(p={}){return oo(S,p)}};return O}const zo=K("empty",`
 display: flex;
 flex-direction: column;
 align-items: center;
 font-size: var(--n-font-size);
`,[L("icon",`
 width: var(--n-icon-size);
 height: var(--n-icon-size);
 font-size: var(--n-icon-size);
 line-height: var(--n-icon-size);
 color: var(--n-icon-color);
 transition:
 color .3s var(--n-bezier);
 `,[ce("+",[L("description",`
 margin-top: 8px;
 `)])]),L("description",`
 transition: color .3s var(--n-bezier);
 color: var(--n-text-color);
 `),L("extra",`
 text-align: center;
 transition: color .3s var(--n-bezier);
 margin-top: 12px;
 color: var(--n-extra-text-color);
 `)]),Fo=Object.assign(Object.assign({},be.props),{description:String,showDescription:{type:Boolean,default:!0},showIcon:{type:Boolean,default:!0},size:{type:String,default:"medium"},renderIcon:Function}),Io=ue({name:"Empty",props:Fo,setup(e){const{mergedClsPrefixRef:n,inlineThemeDisabled:t}=Le(e),r=be("Empty","-empty",zo,wn,e,n),{localeRef:o}=Lt("Empty"),s=bt(Cn,null),a=E(()=>{var c,v,S;return(c=e.description)!==null&&c!==void 0?c:(S=(v=s==null?void 0:s.mergedComponentPropsRef.value)===null||v===void 0?void 0:v.Empty)===null||S===void 0?void 0:S.description}),i=E(()=>{var c,v;return((v=(c=s==null?void 0:s.mergedComponentPropsRef.value)===null||c===void 0?void 0:c.Empty)===null||v===void 0?void 0:v.renderIcon)||(()=>d(to,null))}),h=E(()=>{const{size:c}=e,{common:{cubicBezierEaseInOut:v},self:{[Q("iconSize",c)]:S,[Q("fontSize",c)]:y,textColor:g,iconColor:T,extraTextColor:M}}=r.value;return{"--n-icon-size":S,"--n-font-size":y,"--n-bezier":v,"--n-text-color":g,"--n-icon-color":T,"--n-extra-text-color":M}}),b=t?De("empty",E(()=>{let c="";const{size:v}=e;return c+=v[0],c}),h,e):void 0;return{mergedClsPrefix:n,mergedRenderIcon:i,localizedDescription:E(()=>a.value||o.value.description),cssVars:t?void 0:h,themeClass:b==null?void 0:b.themeClass,onRender:b==null?void 0:b.onRender}},render(){const{$slots:e,mergedClsPrefix:n,onRender:t}=this;return t==null||t(),d("div",{class:[`${n}-empty`,this.themeClass],style:this.cssVars},this.showIcon?d("div",{class:`${n}-empty__icon`},e.icon?e.icon():d(Et,{clsPrefix:n},{default:this.mergedRenderIcon})):null,this.showDescription?d("div",{class:`${n}-empty__description`},e.default?e.default():this.localizedDescription):null,e.extra?d("div",{class:`${n}-empty__extra`},e.extra()):null)}});function Mo(e,n){return d($t,{name:"fade-in-scale-up-transition"},{default:()=>e?d(Et,{clsPrefix:n,class:`${n}-base-select-option__check`},{default:()=>d(eo)}):null})}const It=ue({name:"NBaseSelectOption",props:{clsPrefix:{type:String,required:!0},tmNode:{type:Object,required:!0}},setup(e){const{valueRef:n,pendingTmNodeRef:t,multipleRef:r,valueSetRef:o,renderLabelRef:s,renderOptionRef:a,labelFieldRef:i,valueFieldRef:h,showCheckmarkRef:b,nodePropsRef:c,handleOptionClick:v,handleOptionMouseEnter:S}=bt(mt),y=Ze(()=>{const{value:z}=t;return z?e.tmNode.key===z.key:!1});function g(z){const{tmNode:F}=e;F.disabled||v(z,F)}function T(z){const{tmNode:F}=e;F.disabled||S(z,F)}function M(z){const{tmNode:F}=e,{value:O}=y;F.disabled||O||S(z,F)}return{multiple:r,isGrouped:Ze(()=>{const{tmNode:z}=e,{parent:F}=z;return F&&F.rawNode.type==="group"}),showCheckmark:b,nodeProps:c,isPending:y,isSelected:Ze(()=>{const{value:z}=n,{value:F}=r;if(z===null)return!1;const O=e.tmNode.rawNode[h.value];if(F){const{value:p}=o;return p.has(O)}else return z===O}),labelField:i,renderLabel:s,renderOption:a,handleMouseMove:M,handleMouseEnter:T,handleClick:g}},render(){const{clsPrefix:e,tmNode:{rawNode:n},isSelected:t,isPending:r,isGrouped:o,showCheckmark:s,nodeProps:a,renderOption:i,renderLabel:h,handleClick:b,handleMouseEnter:c,handleMouseMove:v}=this,S=Mo(t,e),y=h?[h(n,t),s&&S]:[Te(n[this.labelField],n,t),s&&S],g=a==null?void 0:a(n),T=d("div",Object.assign({},g,{class:[`${e}-base-select-option`,n.class,g==null?void 0:g.class,{[`${e}-base-select-option--disabled`]:n.disabled,[`${e}-base-select-option--selected`]:t,[`${e}-base-select-option--grouped`]:o,[`${e}-base-select-option--pending`]:r,[`${e}-base-select-option--show-checkmark`]:s}],style:[(g==null?void 0:g.style)||"",n.style||""],onClick:rt([b,g==null?void 0:g.onClick]),onMouseenter:rt([c,g==null?void 0:g.onMouseenter]),onMousemove:rt([v,g==null?void 0:g.onMousemove])}),d("div",{class:`${e}-base-select-option__content`},y));return n.render?n.render({node:T,option:n,selected:t}):i?i({node:T,option:n,selected:t}):T}}),Mt=ue({name:"NBaseSelectGroupHeader",props:{clsPrefix:{type:String,required:!0},tmNode:{type:Object,required:!0}},setup(){const{renderLabelRef:e,renderOptionRef:n,labelFieldRef:t,nodePropsRef:r}=bt(mt);return{labelField:t,nodeProps:r,renderLabel:e,renderOption:n}},render(){const{clsPrefix:e,renderLabel:n,renderOption:t,nodeProps:r,tmNode:{rawNode:o}}=this,s=r==null?void 0:r(o),a=n?n(o,!1):Te(o[this.labelField],o,!1),i=d("div",Object.assign({},s,{class:[`${e}-base-select-group-header`,s==null?void 0:s.class]}),a);return o.render?o.render({node:i,option:o}):t?t({node:i,option:o,selected:!1}):i}}),Bo=K("base-select-menu",`
 line-height: 1.5;
 outline: none;
 z-index: 0;
 position: relative;
 border-radius: var(--n-border-radius);
 transition:
 background-color .3s var(--n-bezier),
 box-shadow .3s var(--n-bezier);
 background-color: var(--n-color);
`,[K("scrollbar",`
 max-height: var(--n-height);
 `),K("virtual-list",`
 max-height: var(--n-height);
 `),K("base-select-option",`
 min-height: var(--n-option-height);
 font-size: var(--n-option-font-size);
 display: flex;
 align-items: center;
 `,[L("content",`
 z-index: 1;
 white-space: nowrap;
 text-overflow: ellipsis;
 overflow: hidden;
 `)]),K("base-select-group-header",`
 min-height: var(--n-option-height);
 font-size: .93em;
 display: flex;
 align-items: center;
 `),K("base-select-menu-option-wrapper",`
 position: relative;
 width: 100%;
 `),L("loading, empty",`
 display: flex;
 padding: 12px 32px;
 flex: 1;
 justify-content: center;
 `),L("loading",`
 color: var(--n-loading-color);
 font-size: var(--n-loading-size);
 `),L("header",`
 padding: 8px var(--n-option-padding-left);
 font-size: var(--n-option-font-size);
 transition: 
 color .3s var(--n-bezier),
 border-color .3s var(--n-bezier);
 border-bottom: 1px solid var(--n-action-divider-color);
 color: var(--n-action-text-color);
 `),L("action",`
 padding: 8px var(--n-option-padding-left);
 font-size: var(--n-option-font-size);
 transition: 
 color .3s var(--n-bezier),
 border-color .3s var(--n-bezier);
 border-top: 1px solid var(--n-action-divider-color);
 color: var(--n-action-text-color);
 `),K("base-select-group-header",`
 position: relative;
 cursor: default;
 padding: var(--n-option-padding);
 color: var(--n-group-header-text-color);
 `),K("base-select-option",`
 cursor: pointer;
 position: relative;
 padding: var(--n-option-padding);
 transition:
 color .3s var(--n-bezier),
 opacity .3s var(--n-bezier);
 box-sizing: border-box;
 color: var(--n-option-text-color);
 opacity: 1;
 `,[ee("show-checkmark",`
 padding-right: calc(var(--n-option-padding-right) + 20px);
 `),ce("&::before",`
 content: "";
 position: absolute;
 left: 4px;
 right: 4px;
 top: 0;
 bottom: 0;
 border-radius: var(--n-border-radius);
 transition: background-color .3s var(--n-bezier);
 `),ce("&:active",`
 color: var(--n-option-text-color-pressed);
 `),ee("grouped",`
 padding-left: calc(var(--n-option-padding-left) * 1.5);
 `),ee("pending",[ce("&::before",`
 background-color: var(--n-option-color-pending);
 `)]),ee("selected",`
 color: var(--n-option-text-color-active);
 `,[ce("&::before",`
 background-color: var(--n-option-color-active);
 `),ee("pending",[ce("&::before",`
 background-color: var(--n-option-color-active-pending);
 `)])]),ee("disabled",`
 cursor: not-allowed;
 `,[Re("selected",`
 color: var(--n-option-text-color-disabled);
 `),ee("selected",`
 opacity: var(--n-option-opacity-disabled);
 `)]),L("check",`
 font-size: 16px;
 position: absolute;
 right: calc(var(--n-option-padding-right) - 4px);
 top: calc(50% - 7px);
 color: var(--n-option-check-color);
 transition: color .3s var(--n-bezier);
 `,[At({enterScale:"0.5"})])])]),_o=ue({name:"InternalSelectMenu",props:Object.assign(Object.assign({},be.props),{clsPrefix:{type:String,required:!0},scrollable:{type:Boolean,default:!0},treeMate:{type:Object,required:!0},multiple:Boolean,size:{type:String,default:"medium"},value:{type:[String,Number,Array],default:null},autoPending:Boolean,virtualScroll:{type:Boolean,default:!0},show:{type:Boolean,default:!0},labelField:{type:String,default:"label"},valueField:{type:String,default:"value"},loading:Boolean,focusable:Boolean,renderLabel:Function,renderOption:Function,nodeProps:Function,showCheckmark:{type:Boolean,default:!0},onMousedown:Function,onScroll:Function,onFocus:Function,onBlur:Function,onKeyup:Function,onKeydown:Function,onTabOut:Function,onMouseenter:Function,onMouseleave:Function,onResize:Function,resetMenuOnOptionsChange:{type:Boolean,default:!0},inlineThemeDisabled:Boolean,onToggle:Function}),setup(e){const{mergedClsPrefixRef:n,mergedRtlRef:t}=Le(e),r=pt("InternalSelectMenu",t,n),o=be("InternalSelectMenu","-internal-select-menu",Bo,xn,e,ie(e,"clsPrefix")),s=_(null),a=_(null),i=_(null),h=E(()=>e.treeMate.getFlattenedNodes()),b=E(()=>go(h.value)),c=_(null);function v(){const{treeMate:u}=e;let x=null;const{value:J}=e;J===null?x=u.getFirstAvailableNode():(e.multiple?x=u.getNode((J||[])[(J||[]).length-1]):x=u.getNode(J),(!x||x.disabled)&&(x=u.getFirstAvailableNode())),W(x||null)}function S(){const{value:u}=c;u&&!e.treeMate.getNode(u.key)&&(c.value=null)}let y;Oe(()=>e.show,u=>{u?y=Oe(()=>e.treeMate,()=>{e.resetMenuOnOptionsChange?(e.autoPending?v():S(),gt(G)):S()},{immediate:!0}):y==null||y()},{immediate:!0}),_t(()=>{y==null||y()});const g=E(()=>ut(o.value.self[Q("optionHeight",e.size)])),T=E(()=>ze(o.value.self[Q("padding",e.size)])),M=E(()=>e.multiple&&Array.isArray(e.value)?new Set(e.value):new Set),z=E(()=>{const u=h.value;return u&&u.length===0});function F(u){const{onToggle:x}=e;x&&x(u)}function O(u){const{onScroll:x}=e;x&&x(u)}function p(u){var x;(x=i.value)===null||x===void 0||x.sync(),O(u)}function m(){var u;(u=i.value)===null||u===void 0||u.sync()}function $(){const{value:u}=c;return u||null}function A(u,x){x.disabled||W(x,!1)}function j(u,x){x.disabled||F(x)}function U(u){var x;Ae(u,"action")||(x=e.onKeyup)===null||x===void 0||x.call(e,u)}function B(u){var x;Ae(u,"action")||(x=e.onKeydown)===null||x===void 0||x.call(e,u)}function w(u){var x;(x=e.onMousedown)===null||x===void 0||x.call(e,u),!e.focusable&&u.preventDefault()}function R(){const{value:u}=c;u&&W(u.getNext({loop:!0}),!0)}function P(){const{value:u}=c;u&&W(u.getPrev({loop:!0}),!0)}function W(u,x=!1){c.value=u,x&&G()}function G(){var u,x;const J=c.value;if(!J)return;const fe=b.value(J.key);fe!==null&&(e.virtualScroll?(u=a.value)===null||u===void 0||u.scrollTo({index:fe}):(x=i.value)===null||x===void 0||x.scrollTo({index:fe,elSize:g.value}))}function q(u){var x,J;!((x=s.value)===null||x===void 0)&&x.contains(u.target)&&((J=e.onFocus)===null||J===void 0||J.call(e,u))}function Z(u){var x,J;!((x=s.value)===null||x===void 0)&&x.contains(u.relatedTarget)||(J=e.onBlur)===null||J===void 0||J.call(e,u)}ft(mt,{handleOptionMouseEnter:A,handleOptionClick:j,valueSetRef:M,pendingTmNodeRef:c,nodePropsRef:ie(e,"nodeProps"),showCheckmarkRef:ie(e,"showCheckmark"),multipleRef:ie(e,"multiple"),valueRef:ie(e,"value"),renderLabelRef:ie(e,"renderLabel"),renderOptionRef:ie(e,"renderOption"),labelFieldRef:ie(e,"labelField"),valueFieldRef:ie(e,"valueField")}),ft(Kn,s),Ne(()=>{const{value:u}=i;u&&u.sync()});const Y=E(()=>{const{size:u}=e,{common:{cubicBezierEaseInOut:x},self:{height:J,borderRadius:fe,color:pe,groupHeaderTextColor:ye,actionDividerColor:he,optionTextColorPressed:ae,optionTextColor:Ce,optionTextColorDisabled:de,optionTextColorActive:Fe,optionOpacityDisabled:Ie,optionCheckColor:Me,actionTextColor:Be,optionColorPending:xe,optionColorActive:Se,loadingColor:_e,loadingSize:Ee,optionColorActivePending:$e,[Q("optionFontSize",u)]:Pe,[Q("optionHeight",u)]:ke,[Q("optionPadding",u)]:se}}=o.value;return{"--n-height":J,"--n-action-divider-color":he,"--n-action-text-color":Be,"--n-bezier":x,"--n-border-radius":fe,"--n-color":pe,"--n-option-font-size":Pe,"--n-group-header-text-color":ye,"--n-option-check-color":Me,"--n-option-color-pending":xe,"--n-option-color-active":Se,"--n-option-color-active-pending":$e,"--n-option-height":ke,"--n-option-opacity-disabled":Ie,"--n-option-text-color":Ce,"--n-option-text-color-active":Fe,"--n-option-text-color-disabled":de,"--n-option-text-color-pressed":ae,"--n-option-padding":se,"--n-option-padding-left":ze(se,"left"),"--n-option-padding-right":ze(se,"right"),"--n-loading-color":_e,"--n-loading-size":Ee}}),{inlineThemeDisabled:le}=e,te=le?De("internal-select-menu",E(()=>e.size[0]),Y,e):void 0,re={selfRef:s,next:R,prev:P,getPendingTmNode:$};return Dt(s,e.onResize),Object.assign({mergedTheme:o,mergedClsPrefix:n,rtlEnabled:r,virtualListRef:a,scrollbarRef:i,itemSize:g,padding:T,flattenedNodes:h,empty:z,virtualListContainer(){const{value:u}=a;return u==null?void 0:u.listElRef},virtualListContent(){const{value:u}=a;return u==null?void 0:u.itemsElRef},doScroll:O,handleFocusin:q,handleFocusout:Z,handleKeyUp:U,handleKeyDown:B,handleMouseDown:w,handleVirtualListResize:m,handleVirtualListScroll:p,cssVars:le?void 0:Y,themeClass:te==null?void 0:te.themeClass,onRender:te==null?void 0:te.onRender},re)},render(){const{$slots:e,virtualScroll:n,clsPrefix:t,mergedTheme:r,themeClass:o,onRender:s}=this;return s==null||s(),d("div",{ref:"selfRef",tabindex:this.focusable?0:-1,class:[`${t}-base-select-menu`,this.rtlEnabled&&`${t}-base-select-menu--rtl`,o,this.multiple&&`${t}-base-select-menu--multiple`],style:this.cssVars,onFocusin:this.handleFocusin,onFocusout:this.handleFocusout,onKeyup:this.handleKeyUp,onKeydown:this.handleKeyDown,onMousedown:this.handleMouseDown,onMouseenter:this.onMouseenter,onMouseleave:this.onMouseleave},Xe(e.header,a=>a&&d("div",{class:`${t}-base-select-menu__header`,"data-header":!0,key:"header"},a)),this.loading?d("div",{class:`${t}-base-select-menu__loading`},d(Sn,{clsPrefix:t,strokeWidth:20})):this.empty?d("div",{class:`${t}-base-select-menu__empty`,"data-empty":!0},Rn(e.empty,()=>[d(Io,{theme:r.peers.Empty,themeOverrides:r.peerOverrides.Empty})])):d(kn,{ref:"scrollbarRef",theme:r.peers.Scrollbar,themeOverrides:r.peerOverrides.Scrollbar,scrollable:this.scrollable,container:n?this.virtualListContainer:void 0,content:n?this.virtualListContent:void 0,onScroll:n?void 0:this.doScroll},{default:()=>n?d(Jn,{ref:"virtualListRef",class:`${t}-virtual-list`,items:this.flattenedNodes,itemSize:this.itemSize,showScrollbar:!1,paddingTop:this.padding.top,paddingBottom:this.padding.bottom,onResize:this.handleVirtualListResize,onScroll:this.handleVirtualListScroll,itemResizable:!0},{default:({item:a})=>a.isGroup?d(Mt,{key:a.key,clsPrefix:t,tmNode:a}):a.ignored?null:d(It,{clsPrefix:t,key:a.key,tmNode:a})}):d("div",{class:`${t}-base-select-menu-option-wrapper`,style:{paddingTop:this.padding.top,paddingBottom:this.padding.bottom}},this.flattenedNodes.map(a=>a.isGroup?d(Mt,{key:a.key,clsPrefix:t,tmNode:a}):d(It,{clsPrefix:t,key:a.key,tmNode:a})))}),Xe(e.action,a=>a&&[d("div",{class:`${t}-base-select-menu__action`,"data-action":!0,key:"action"},a),d(no,{onFocus:this.onTabOut,key:"focus-detector"})]))}});function Eo(e){const{textColor2:n,primaryColorHover:t,primaryColorPressed:r,primaryColor:o,infoColor:s,successColor:a,warningColor:i,errorColor:h,baseColor:b,borderColor:c,opacityDisabled:v,tagColor:S,closeIconColor:y,closeIconColorHover:g,closeIconColorPressed:T,borderRadiusSmall:M,fontSizeMini:z,fontSizeTiny:F,fontSizeSmall:O,fontSizeMedium:p,heightMini:m,heightTiny:$,heightSmall:A,heightMedium:j,closeColorHover:U,closeColorPressed:B,buttonColor2Hover:w,buttonColor2Pressed:R,fontWeightStrong:P}=e;return Object.assign(Object.assign({},Tn),{closeBorderRadius:M,heightTiny:m,heightSmall:$,heightMedium:A,heightLarge:j,borderRadius:M,opacityDisabled:v,fontSizeTiny:z,fontSizeSmall:F,fontSizeMedium:O,fontSizeLarge:p,fontWeightStrong:P,textColorCheckable:n,textColorHoverCheckable:n,textColorPressedCheckable:n,textColorChecked:b,colorCheckable:"#0000",colorHoverCheckable:w,colorPressedCheckable:R,colorChecked:o,colorCheckedHover:t,colorCheckedPressed:r,border:`1px solid ${c}`,textColor:n,color:S,colorBordered:"rgb(250, 250, 252)",closeIconColor:y,closeIconColorHover:g,closeIconColorPressed:T,closeColorHover:U,closeColorPressed:B,borderPrimary:`1px solid ${X(o,{alpha:.3})}`,textColorPrimary:o,colorPrimary:X(o,{alpha:.12}),colorBorderedPrimary:X(o,{alpha:.1}),closeIconColorPrimary:o,closeIconColorHoverPrimary:o,closeIconColorPressedPrimary:o,closeColorHoverPrimary:X(o,{alpha:.12}),closeColorPressedPrimary:X(o,{alpha:.18}),borderInfo:`1px solid ${X(s,{alpha:.3})}`,textColorInfo:s,colorInfo:X(s,{alpha:.12}),colorBorderedInfo:X(s,{alpha:.1}),closeIconColorInfo:s,closeIconColorHoverInfo:s,closeIconColorPressedInfo:s,closeColorHoverInfo:X(s,{alpha:.12}),closeColorPressedInfo:X(s,{alpha:.18}),borderSuccess:`1px solid ${X(a,{alpha:.3})}`,textColorSuccess:a,colorSuccess:X(a,{alpha:.12}),colorBorderedSuccess:X(a,{alpha:.1}),closeIconColorSuccess:a,closeIconColorHoverSuccess:a,closeIconColorPressedSuccess:a,closeColorHoverSuccess:X(a,{alpha:.12}),closeColorPressedSuccess:X(a,{alpha:.18}),borderWarning:`1px solid ${X(i,{alpha:.35})}`,textColorWarning:i,colorWarning:X(i,{alpha:.15}),colorBorderedWarning:X(i,{alpha:.12}),closeIconColorWarning:i,closeIconColorHoverWarning:i,closeIconColorPressedWarning:i,closeColorHoverWarning:X(i,{alpha:.12}),closeColorPressedWarning:X(i,{alpha:.18}),borderError:`1px solid ${X(h,{alpha:.23})}`,textColorError:h,colorError:X(h,{alpha:.1}),colorBorderedError:X(h,{alpha:.08}),closeIconColorError:h,closeIconColorHoverError:h,closeIconColorPressedError:h,closeColorHoverError:X(h,{alpha:.12}),closeColorPressedError:X(h,{alpha:.18})})}const $o={name:"Tag",common:Pn,self:Eo},Ao=$o,No={color:Object,type:{type:String,default:"default"},round:Boolean,size:{type:String,default:"medium"},closable:Boolean,disabled:{type:Boolean,default:void 0}},Lo=K("tag",`
 --n-close-margin: var(--n-close-margin-top) var(--n-close-margin-right) var(--n-close-margin-bottom) var(--n-close-margin-left);
 white-space: nowrap;
 position: relative;
 box-sizing: border-box;
 cursor: default;
 display: inline-flex;
 align-items: center;
 flex-wrap: nowrap;
 padding: var(--n-padding);
 border-radius: var(--n-border-radius);
 color: var(--n-text-color);
 background-color: var(--n-color);
 transition: 
 border-color .3s var(--n-bezier),
 background-color .3s var(--n-bezier),
 color .3s var(--n-bezier),
 box-shadow .3s var(--n-bezier),
 opacity .3s var(--n-bezier);
 line-height: 1;
 height: var(--n-height);
 font-size: var(--n-font-size);
`,[ee("strong",`
 font-weight: var(--n-font-weight-strong);
 `),L("border",`
 pointer-events: none;
 position: absolute;
 left: 0;
 right: 0;
 top: 0;
 bottom: 0;
 border-radius: inherit;
 border: var(--n-border);
 transition: border-color .3s var(--n-bezier);
 `),L("icon",`
 display: flex;
 margin: 0 4px 0 0;
 color: var(--n-text-color);
 transition: color .3s var(--n-bezier);
 font-size: var(--n-avatar-size-override);
 `),L("avatar",`
 display: flex;
 margin: 0 6px 0 0;
 `),L("close",`
 margin: var(--n-close-margin);
 transition:
 background-color .3s var(--n-bezier),
 color .3s var(--n-bezier);
 `),ee("round",`
 padding: 0 calc(var(--n-height) / 3);
 border-radius: calc(var(--n-height) / 2);
 `,[L("icon",`
 margin: 0 4px 0 calc((var(--n-height) - 8px) / -2);
 `),L("avatar",`
 margin: 0 6px 0 calc((var(--n-height) - 8px) / -2);
 `),ee("closable",`
 padding: 0 calc(var(--n-height) / 4) 0 calc(var(--n-height) / 3);
 `)]),ee("icon, avatar",[ee("round",`
 padding: 0 calc(var(--n-height) / 3) 0 calc(var(--n-height) / 2);
 `)]),ee("disabled",`
 cursor: not-allowed !important;
 opacity: var(--n-opacity-disabled);
 `),ee("checkable",`
 cursor: pointer;
 box-shadow: none;
 color: var(--n-text-color-checkable);
 background-color: var(--n-color-checkable);
 `,[Re("disabled",[ce("&:hover","background-color: var(--n-color-hover-checkable);",[Re("checked","color: var(--n-text-color-hover-checkable);")]),ce("&:active","background-color: var(--n-color-pressed-checkable);",[Re("checked","color: var(--n-text-color-pressed-checkable);")])]),ee("checked",`
 color: var(--n-text-color-checked);
 background-color: var(--n-color-checked);
 `,[Re("disabled",[ce("&:hover","background-color: var(--n-color-checked-hover);"),ce("&:active","background-color: var(--n-color-checked-pressed);")])])])]),Do=Object.assign(Object.assign(Object.assign({},be.props),No),{bordered:{type:Boolean,default:void 0},checked:Boolean,checkable:Boolean,strong:Boolean,triggerClickOnClose:Boolean,onClose:[Array,Function],onMouseenter:Function,onMouseleave:Function,"onUpdate:checked":Function,onUpdateChecked:Function,internalCloseFocusable:{type:Boolean,default:!0},internalCloseIsButtonTag:{type:Boolean,default:!0},onCheckedChange:Function}),Ho=zn("n-tag"),ct=ue({name:"Tag",props:Do,setup(e){const n=_(null),{mergedBorderedRef:t,mergedClsPrefixRef:r,inlineThemeDisabled:o,mergedRtlRef:s}=Le(e),a=be("Tag","-tag",Lo,Ao,e,r);ft(Ho,{roundRef:ie(e,"round")});function i(){if(!e.disabled&&e.checkable){const{checked:y,onCheckedChange:g,onUpdateChecked:T,"onUpdate:checked":M}=e;T&&T(!y),M&&M(!y),g&&g(!y)}}function h(y){if(e.triggerClickOnClose||y.stopPropagation(),!e.disabled){const{onClose:g}=e;g&&ve(g,y)}}const b={setTextContent(y){const{value:g}=n;g&&(g.textContent=y)}},c=pt("Tag",s,r),v=E(()=>{const{type:y,size:g,color:{color:T,textColor:M}={}}=e,{common:{cubicBezierEaseInOut:z},self:{padding:F,closeMargin:O,borderRadius:p,opacityDisabled:m,textColorCheckable:$,textColorHoverCheckable:A,textColorPressedCheckable:j,textColorChecked:U,colorCheckable:B,colorHoverCheckable:w,colorPressedCheckable:R,colorChecked:P,colorCheckedHover:W,colorCheckedPressed:G,closeBorderRadius:q,fontWeightStrong:Z,[Q("colorBordered",y)]:Y,[Q("closeSize",g)]:le,[Q("closeIconSize",g)]:te,[Q("fontSize",g)]:re,[Q("height",g)]:u,[Q("color",y)]:x,[Q("textColor",y)]:J,[Q("border",y)]:fe,[Q("closeIconColor",y)]:pe,[Q("closeIconColorHover",y)]:ye,[Q("closeIconColorPressed",y)]:he,[Q("closeColorHover",y)]:ae,[Q("closeColorPressed",y)]:Ce}}=a.value,de=ze(O);return{"--n-font-weight-strong":Z,"--n-avatar-size-override":`calc(${u} - 8px)`,"--n-bezier":z,"--n-border-radius":p,"--n-border":fe,"--n-close-icon-size":te,"--n-close-color-pressed":Ce,"--n-close-color-hover":ae,"--n-close-border-radius":q,"--n-close-icon-color":pe,"--n-close-icon-color-hover":ye,"--n-close-icon-color-pressed":he,"--n-close-icon-color-disabled":pe,"--n-close-margin-top":de.top,"--n-close-margin-right":de.right,"--n-close-margin-bottom":de.bottom,"--n-close-margin-left":de.left,"--n-close-size":le,"--n-color":T||(t.value?Y:x),"--n-color-checkable":B,"--n-color-checked":P,"--n-color-checked-hover":W,"--n-color-checked-pressed":G,"--n-color-hover-checkable":w,"--n-color-pressed-checkable":R,"--n-font-size":re,"--n-height":u,"--n-opacity-disabled":m,"--n-padding":F,"--n-text-color":M||J,"--n-text-color-checkable":$,"--n-text-color-checked":U,"--n-text-color-hover-checkable":A,"--n-text-color-pressed-checkable":j}}),S=o?De("tag",E(()=>{let y="";const{type:g,size:T,color:{color:M,textColor:z}={}}=e;return y+=g[0],y+=T[0],M&&(y+=`a${xt(M)}`),z&&(y+=`b${xt(z)}`),t.value&&(y+="c"),y}),v,e):void 0;return Object.assign(Object.assign({},b),{rtlEnabled:c,mergedClsPrefix:r,contentRef:n,mergedBordered:t,handleClick:i,handleCloseClick:h,cssVars:o?void 0:v,themeClass:S==null?void 0:S.themeClass,onRender:S==null?void 0:S.onRender})},render(){var e,n;const{mergedClsPrefix:t,rtlEnabled:r,closable:o,color:{borderColor:s}={},round:a,onRender:i,$slots:h}=this;i==null||i();const b=Xe(h.avatar,v=>v&&d("div",{class:`${t}-tag__avatar`},v)),c=Xe(h.icon,v=>v&&d("div",{class:`${t}-tag__icon`},v));return d("div",{class:[`${t}-tag`,this.themeClass,{[`${t}-tag--rtl`]:r,[`${t}-tag--strong`]:this.strong,[`${t}-tag--disabled`]:this.disabled,[`${t}-tag--checkable`]:this.checkable,[`${t}-tag--checked`]:this.checkable&&this.checked,[`${t}-tag--round`]:a,[`${t}-tag--avatar`]:b,[`${t}-tag--icon`]:c,[`${t}-tag--closable`]:o}],style:this.cssVars,onClick:this.handleClick,onMouseenter:this.onMouseenter,onMouseleave:this.onMouseleave},c||b,d("span",{class:`${t}-tag__content`,ref:"contentRef"},(n=(e=this.$slots).default)===null||n===void 0?void 0:n.call(e)),!this.checkable&&o?d(On,{clsPrefix:t,class:`${t}-tag__close`,disabled:this.disabled,onClick:this.handleCloseClick,focusable:this.internalCloseFocusable,round:a,isButtonTag:this.internalCloseIsButtonTag,absolute:!0}):null,!this.checkable&&this.mergedBordered?d("div",{class:`${t}-tag__border`,style:{borderColor:s}}):null)}}),Ko=ce([K("base-selection",`
 --n-padding-single: var(--n-padding-single-top) var(--n-padding-single-right) var(--n-padding-single-bottom) var(--n-padding-single-left);
 --n-padding-multiple: var(--n-padding-multiple-top) var(--n-padding-multiple-right) var(--n-padding-multiple-bottom) var(--n-padding-multiple-left);
 position: relative;
 z-index: auto;
 box-shadow: none;
 width: 100%;
 max-width: 100%;
 display: inline-block;
 vertical-align: bottom;
 border-radius: var(--n-border-radius);
 min-height: var(--n-height);
 line-height: 1.5;
 font-size: var(--n-font-size);
 `,[K("base-loading",`
 color: var(--n-loading-color);
 `),K("base-selection-tags","min-height: var(--n-height);"),L("border, state-border",`
 position: absolute;
 left: 0;
 right: 0;
 top: 0;
 bottom: 0;
 pointer-events: none;
 border: var(--n-border);
 border-radius: inherit;
 transition:
 box-shadow .3s var(--n-bezier),
 border-color .3s var(--n-bezier);
 `),L("state-border",`
 z-index: 1;
 border-color: #0000;
 `),K("base-suffix",`
 cursor: pointer;
 position: absolute;
 top: 50%;
 transform: translateY(-50%);
 right: 10px;
 `,[L("arrow",`
 font-size: var(--n-arrow-size);
 color: var(--n-arrow-color);
 transition: color .3s var(--n-bezier);
 `)]),K("base-selection-overlay",`
 display: flex;
 align-items: center;
 white-space: nowrap;
 pointer-events: none;
 position: absolute;
 top: 0;
 right: 0;
 bottom: 0;
 left: 0;
 padding: var(--n-padding-single);
 transition: color .3s var(--n-bezier);
 `,[L("wrapper",`
 flex-basis: 0;
 flex-grow: 1;
 overflow: hidden;
 text-overflow: ellipsis;
 `)]),K("base-selection-placeholder",`
 color: var(--n-placeholder-color);
 `,[L("inner",`
 max-width: 100%;
 overflow: hidden;
 `)]),K("base-selection-tags",`
 cursor: pointer;
 outline: none;
 box-sizing: border-box;
 position: relative;
 z-index: auto;
 display: flex;
 padding: var(--n-padding-multiple);
 flex-wrap: wrap;
 align-items: center;
 width: 100%;
 vertical-align: bottom;
 background-color: var(--n-color);
 border-radius: inherit;
 transition:
 color .3s var(--n-bezier),
 box-shadow .3s var(--n-bezier),
 background-color .3s var(--n-bezier);
 `),K("base-selection-label",`
 height: var(--n-height);
 display: inline-flex;
 width: 100%;
 vertical-align: bottom;
 cursor: pointer;
 outline: none;
 z-index: auto;
 box-sizing: border-box;
 position: relative;
 transition:
 color .3s var(--n-bezier),
 box-shadow .3s var(--n-bezier),
 background-color .3s var(--n-bezier);
 border-radius: inherit;
 background-color: var(--n-color);
 align-items: center;
 `,[K("base-selection-input",`
 font-size: inherit;
 line-height: inherit;
 outline: none;
 cursor: pointer;
 box-sizing: border-box;
 border:none;
 width: 100%;
 padding: var(--n-padding-single);
 background-color: #0000;
 color: var(--n-text-color);
 transition: color .3s var(--n-bezier);
 caret-color: var(--n-caret-color);
 `,[L("content",`
 text-overflow: ellipsis;
 overflow: hidden;
 white-space: nowrap; 
 `)]),L("render-label",`
 color: var(--n-text-color);
 `)]),Re("disabled",[ce("&:hover",[L("state-border",`
 box-shadow: var(--n-box-shadow-hover);
 border: var(--n-border-hover);
 `)]),ee("focus",[L("state-border",`
 box-shadow: var(--n-box-shadow-focus);
 border: var(--n-border-focus);
 `)]),ee("active",[L("state-border",`
 box-shadow: var(--n-box-shadow-active);
 border: var(--n-border-active);
 `),K("base-selection-label","background-color: var(--n-color-active);"),K("base-selection-tags","background-color: var(--n-color-active);")])]),ee("disabled","cursor: not-allowed;",[L("arrow",`
 color: var(--n-arrow-color-disabled);
 `),K("base-selection-label",`
 cursor: not-allowed;
 background-color: var(--n-color-disabled);
 `,[K("base-selection-input",`
 cursor: not-allowed;
 color: var(--n-text-color-disabled);
 `),L("render-label",`
 color: var(--n-text-color-disabled);
 `)]),K("base-selection-tags",`
 cursor: not-allowed;
 background-color: var(--n-color-disabled);
 `),K("base-selection-placeholder",`
 cursor: not-allowed;
 color: var(--n-placeholder-color-disabled);
 `)]),K("base-selection-input-tag",`
 height: calc(var(--n-height) - 6px);
 line-height: calc(var(--n-height) - 6px);
 outline: none;
 display: none;
 position: relative;
 margin-bottom: 3px;
 max-width: 100%;
 vertical-align: bottom;
 `,[L("input",`
 font-size: inherit;
 font-family: inherit;
 min-width: 1px;
 padding: 0;
 background-color: #0000;
 outline: none;
 border: none;
 max-width: 100%;
 overflow: hidden;
 width: 1em;
 line-height: inherit;
 cursor: pointer;
 color: var(--n-text-color);
 caret-color: var(--n-caret-color);
 `),L("mirror",`
 position: absolute;
 left: 0;
 top: 0;
 white-space: pre;
 visibility: hidden;
 user-select: none;
 -webkit-user-select: none;
 opacity: 0;
 `)]),["warning","error"].map(e=>ee(`${e}-status`,[L("state-border",`border: var(--n-border-${e});`),Re("disabled",[ce("&:hover",[L("state-border",`
 box-shadow: var(--n-box-shadow-hover-${e});
 border: var(--n-border-hover-${e});
 `)]),ee("active",[L("state-border",`
 box-shadow: var(--n-box-shadow-active-${e});
 border: var(--n-border-active-${e});
 `),K("base-selection-label",`background-color: var(--n-color-active-${e});`),K("base-selection-tags",`background-color: var(--n-color-active-${e});`)]),ee("focus",[L("state-border",`
 box-shadow: var(--n-box-shadow-focus-${e});
 border: var(--n-border-focus-${e});
 `)])])]))]),K("base-selection-popover",`
 margin-bottom: -3px;
 display: flex;
 flex-wrap: wrap;
 margin-right: -8px;
 `),K("base-selection-tag-wrapper",`
 max-width: 100%;
 display: inline-flex;
 padding: 0 7px 3px 0;
 `,[ce("&:last-child","padding-right: 0;"),K("tag",`
 font-size: 14px;
 max-width: 100%;
 `,[L("content",`
 line-height: 1.25;
 text-overflow: ellipsis;
 overflow: hidden;
 `)])])]),Vo=ue({name:"InternalSelection",props:Object.assign(Object.assign({},be.props),{clsPrefix:{type:String,required:!0},bordered:{type:Boolean,default:void 0},active:Boolean,pattern:{type:String,default:""},placeholder:String,selectedOption:{type:Object,default:null},selectedOptions:{type:Array,default:null},labelField:{type:String,default:"label"},valueField:{type:String,default:"value"},multiple:Boolean,filterable:Boolean,clearable:Boolean,disabled:Boolean,size:{type:String,default:"medium"},loading:Boolean,autofocus:Boolean,showArrow:{type:Boolean,default:!0},inputProps:Object,focused:Boolean,renderTag:Function,onKeydown:Function,onClick:Function,onBlur:Function,onFocus:Function,onDeleteOption:Function,maxTagCount:[String,Number],ellipsisTagPopoverProps:Object,onClear:Function,onPatternInput:Function,onPatternFocus:Function,onPatternBlur:Function,renderLabel:Function,status:String,inlineThemeDisabled:Boolean,ignoreComposition:{type:Boolean,default:!0},onResize:Function}),setup(e){const{mergedClsPrefixRef:n,mergedRtlRef:t}=Le(e),r=pt("InternalSelection",t,n),o=_(null),s=_(null),a=_(null),i=_(null),h=_(null),b=_(null),c=_(null),v=_(null),S=_(null),y=_(null),g=_(!1),T=_(!1),M=_(!1),z=be("InternalSelection","-internal-selection",Ko,Fn,e,ie(e,"clsPrefix")),F=E(()=>e.clearable&&!e.disabled&&(M.value||e.active)),O=E(()=>e.selectedOption?e.renderTag?e.renderTag({option:e.selectedOption,handleClose:()=>{}}):e.renderLabel?e.renderLabel(e.selectedOption,!0):Te(e.selectedOption[e.labelField],e.selectedOption,!0):e.placeholder),p=E(()=>{const f=e.selectedOption;if(f)return f[e.labelField]}),m=E(()=>e.multiple?!!(Array.isArray(e.selectedOptions)&&e.selectedOptions.length):e.selectedOption!==null);function $(){var f;const{value:k}=o;if(k){const{value:ne}=s;ne&&(ne.style.width=`${k.offsetWidth}px`,e.maxTagCount!=="responsive"&&((f=S.value)===null||f===void 0||f.sync({showAllItemsBeforeCalculate:!1})))}}function A(){const{value:f}=y;f&&(f.style.display="none")}function j(){const{value:f}=y;f&&(f.style.display="inline-block")}Oe(ie(e,"active"),f=>{f||A()}),Oe(ie(e,"pattern"),()=>{e.multiple&&gt($)});function U(f){const{onFocus:k}=e;k&&k(f)}function B(f){const{onBlur:k}=e;k&&k(f)}function w(f){const{onDeleteOption:k}=e;k&&k(f)}function R(f){const{onClear:k}=e;k&&k(f)}function P(f){const{onPatternInput:k}=e;k&&k(f)}function W(f){var k;(!f.relatedTarget||!(!((k=a.value)===null||k===void 0)&&k.contains(f.relatedTarget)))&&U(f)}function G(f){var k;!((k=a.value)===null||k===void 0)&&k.contains(f.relatedTarget)||B(f)}function q(f){R(f)}function Z(){M.value=!0}function Y(){M.value=!1}function le(f){!e.active||!e.filterable||f.target!==s.value&&f.preventDefault()}function te(f){w(f)}const re=_(!1);function u(f){if(f.key==="Backspace"&&!re.value&&!e.pattern.length){const{selectedOptions:k}=e;k!=null&&k.length&&te(k[k.length-1])}}let x=null;function J(f){const{value:k}=o;if(k){const ne=f.target.value;k.textContent=ne,$()}e.ignoreComposition&&re.value?x=f:P(f)}function fe(){re.value=!0}function pe(){re.value=!1,e.ignoreComposition&&P(x),x=null}function ye(f){var k;T.value=!0,(k=e.onPatternFocus)===null||k===void 0||k.call(e,f)}function he(f){var k;T.value=!1,(k=e.onPatternBlur)===null||k===void 0||k.call(e,f)}function ae(){var f,k;if(e.filterable)T.value=!1,(f=b.value)===null||f===void 0||f.blur(),(k=s.value)===null||k===void 0||k.blur();else if(e.multiple){const{value:ne}=i;ne==null||ne.blur()}else{const{value:ne}=h;ne==null||ne.blur()}}function Ce(){var f,k,ne;e.filterable?(T.value=!1,(f=b.value)===null||f===void 0||f.focus()):e.multiple?(k=i.value)===null||k===void 0||k.focus():(ne=h.value)===null||ne===void 0||ne.focus()}function de(){const{value:f}=s;f&&(j(),f.focus())}function Fe(){const{value:f}=s;f&&f.blur()}function Ie(f){const{value:k}=c;k&&k.setTextContent(`+${f}`)}function Me(){const{value:f}=v;return f}function Be(){return s.value}let xe=null;function Se(){xe!==null&&window.clearTimeout(xe)}function _e(){e.active||(Se(),xe=window.setTimeout(()=>{m.value&&(g.value=!0)},100))}function Ee(){Se()}function $e(f){f||(Se(),g.value=!1)}Oe(m,f=>{f||(g.value=!1)}),Ne(()=>{In(()=>{const f=b.value;f&&(e.disabled?f.removeAttribute("tabindex"):f.tabIndex=T.value?-1:0)})}),Dt(a,e.onResize);const{inlineThemeDisabled:Pe}=e,ke=E(()=>{const{size:f}=e,{common:{cubicBezierEaseInOut:k},self:{borderRadius:ne,color:et,placeholderColor:tt,textColor:He,paddingSingle:Ke,paddingMultiple:Ve,caretColor:nt,colorDisabled:ot,textColorDisabled:We,placeholderColorDisabled:we,colorActive:l,boxShadowFocus:C,boxShadowActive:I,boxShadowHover:V,border:D,borderFocus:N,borderHover:H,borderActive:oe,arrowColor:ge,arrowColorDisabled:Wt,loadingColor:jt,colorActiveWarning:Ut,boxShadowFocusWarning:Gt,boxShadowActiveWarning:qt,boxShadowHoverWarning:Zt,borderWarning:Yt,borderFocusWarning:Xt,borderHoverWarning:Jt,borderActiveWarning:Qt,colorActiveError:en,boxShadowFocusError:tn,boxShadowActiveError:nn,boxShadowHoverError:on,borderError:rn,borderFocusError:ln,borderHoverError:an,borderActiveError:sn,clearColor:cn,clearColorHover:dn,clearColorPressed:un,clearSize:fn,arrowSize:hn,[Q("height",f)]:vn,[Q("fontSize",f)]:gn}}=z.value,je=ze(Ke),Ue=ze(Ve);return{"--n-bezier":k,"--n-border":D,"--n-border-active":oe,"--n-border-focus":N,"--n-border-hover":H,"--n-border-radius":ne,"--n-box-shadow-active":I,"--n-box-shadow-focus":C,"--n-box-shadow-hover":V,"--n-caret-color":nt,"--n-color":et,"--n-color-active":l,"--n-color-disabled":ot,"--n-font-size":gn,"--n-height":vn,"--n-padding-single-top":je.top,"--n-padding-multiple-top":Ue.top,"--n-padding-single-right":je.right,"--n-padding-multiple-right":Ue.right,"--n-padding-single-left":je.left,"--n-padding-multiple-left":Ue.left,"--n-padding-single-bottom":je.bottom,"--n-padding-multiple-bottom":Ue.bottom,"--n-placeholder-color":tt,"--n-placeholder-color-disabled":we,"--n-text-color":He,"--n-text-color-disabled":We,"--n-arrow-color":ge,"--n-arrow-color-disabled":Wt,"--n-loading-color":jt,"--n-color-active-warning":Ut,"--n-box-shadow-focus-warning":Gt,"--n-box-shadow-active-warning":qt,"--n-box-shadow-hover-warning":Zt,"--n-border-warning":Yt,"--n-border-focus-warning":Xt,"--n-border-hover-warning":Jt,"--n-border-active-warning":Qt,"--n-color-active-error":en,"--n-box-shadow-focus-error":tn,"--n-box-shadow-active-error":nn,"--n-box-shadow-hover-error":on,"--n-border-error":rn,"--n-border-focus-error":ln,"--n-border-hover-error":an,"--n-border-active-error":sn,"--n-clear-size":fn,"--n-clear-color":cn,"--n-clear-color-hover":dn,"--n-clear-color-pressed":un,"--n-arrow-size":hn}}),se=Pe?De("internal-selection",E(()=>e.size[0]),ke,e):void 0;return{mergedTheme:z,mergedClearable:F,mergedClsPrefix:n,rtlEnabled:r,patternInputFocused:T,filterablePlaceholder:O,label:p,selected:m,showTagsPanel:g,isComposing:re,counterRef:c,counterWrapperRef:v,patternInputMirrorRef:o,patternInputRef:s,selfRef:a,multipleElRef:i,singleElRef:h,patternInputWrapperRef:b,overflowRef:S,inputTagElRef:y,handleMouseDown:le,handleFocusin:W,handleClear:q,handleMouseEnter:Z,handleMouseLeave:Y,handleDeleteOption:te,handlePatternKeyDown:u,handlePatternInputInput:J,handlePatternInputBlur:he,handlePatternInputFocus:ye,handleMouseEnterCounter:_e,handleMouseLeaveCounter:Ee,handleFocusout:G,handleCompositionEnd:pe,handleCompositionStart:fe,onPopoverUpdateShow:$e,focus:Ce,focusInput:de,blur:ae,blurInput:Fe,updateCounter:Ie,getCounter:Me,getTail:Be,renderLabel:e.renderLabel,cssVars:Pe?void 0:ke,themeClass:se==null?void 0:se.themeClass,onRender:se==null?void 0:se.onRender}},render(){const{status:e,multiple:n,size:t,disabled:r,filterable:o,maxTagCount:s,bordered:a,clsPrefix:i,ellipsisTagPopoverProps:h,onRender:b,renderTag:c,renderLabel:v}=this;b==null||b();const S=s==="responsive",y=typeof s=="number",g=S||y,T=d(Mn,null,{default:()=>d(qn,{clsPrefix:i,loading:this.loading,showArrow:this.showArrow,showClear:this.mergedClearable&&this.selected,onClear:this.handleClear},{default:()=>{var z,F;return(F=(z=this.$slots).arrow)===null||F===void 0?void 0:F.call(z)}})});let M;if(n){const{labelField:z}=this,F=P=>d("div",{class:`${i}-base-selection-tag-wrapper`,key:P.value},c?c({option:P,handleClose:()=>{this.handleDeleteOption(P)}}):d(ct,{size:t,closable:!P.disabled,disabled:r,onClose:()=>{this.handleDeleteOption(P)},internalCloseIsButtonTag:!1,internalCloseFocusable:!1},{default:()=>v?v(P,!0):Te(P[z],P,!0)})),O=()=>(y?this.selectedOptions.slice(0,s):this.selectedOptions).map(F),p=o?d("div",{class:`${i}-base-selection-input-tag`,ref:"inputTagElRef",key:"__input-tag__"},d("input",Object.assign({},this.inputProps,{ref:"patternInputRef",tabindex:-1,disabled:r,value:this.pattern,autofocus:this.autofocus,class:`${i}-base-selection-input-tag__input`,onBlur:this.handlePatternInputBlur,onFocus:this.handlePatternInputFocus,onKeydown:this.handlePatternKeyDown,onInput:this.handlePatternInputInput,onCompositionstart:this.handleCompositionStart,onCompositionend:this.handleCompositionEnd})),d("span",{ref:"patternInputMirrorRef",class:`${i}-base-selection-input-tag__mirror`},this.pattern)):null,m=S?()=>d("div",{class:`${i}-base-selection-tag-wrapper`,ref:"counterWrapperRef"},d(ct,{size:t,ref:"counterRef",onMouseenter:this.handleMouseEnterCounter,onMouseleave:this.handleMouseLeaveCounter,disabled:r})):void 0;let $;if(y){const P=this.selectedOptions.length-s;P>0&&($=d("div",{class:`${i}-base-selection-tag-wrapper`,key:"__counter__"},d(ct,{size:t,ref:"counterRef",onMouseenter:this.handleMouseEnterCounter,disabled:r},{default:()=>`+${P}`})))}const A=S?o?d(Ot,{ref:"overflowRef",updateCounter:this.updateCounter,getCounter:this.getCounter,getTail:this.getTail,style:{width:"100%",display:"flex",overflow:"hidden"}},{default:O,counter:m,tail:()=>p}):d(Ot,{ref:"overflowRef",updateCounter:this.updateCounter,getCounter:this.getCounter,style:{width:"100%",display:"flex",overflow:"hidden"}},{default:O,counter:m}):y&&$?O().concat($):O(),j=g?()=>d("div",{class:`${i}-base-selection-popover`},S?O():this.selectedOptions.map(F)):void 0,U=g?Object.assign({show:this.showTagsPanel,trigger:"hover",overlap:!0,placement:"top",width:"trigger",onUpdateShow:this.onPopoverUpdateShow,theme:this.mergedTheme.peers.Popover,themeOverrides:this.mergedTheme.peerOverrides.Popover},h):null,w=(this.selected?!1:this.active?!this.pattern&&!this.isComposing:!0)?d("div",{class:`${i}-base-selection-placeholder ${i}-base-selection-overlay`},d("div",{class:`${i}-base-selection-placeholder__inner`},this.placeholder)):null,R=o?d("div",{ref:"patternInputWrapperRef",class:`${i}-base-selection-tags`},A,S?null:p,T):d("div",{ref:"multipleElRef",class:`${i}-base-selection-tags`,tabindex:r?void 0:0},A,T);M=d(Bn,null,g?d(Vn,Object.assign({},U,{scrollable:!0,style:"max-height: calc(var(--v-target-height) * 6.6);"}),{trigger:()=>R,default:j}):R,w)}else if(o){const z=this.pattern||this.isComposing,F=this.active?!z:!this.selected,O=this.active?!1:this.selected;M=d("div",{ref:"patternInputWrapperRef",class:`${i}-base-selection-label`,title:this.patternInputFocused?void 0:Rt(this.label)},d("input",Object.assign({},this.inputProps,{ref:"patternInputRef",class:`${i}-base-selection-input`,value:this.active?this.pattern:"",placeholder:"",readonly:r,disabled:r,tabindex:-1,autofocus:this.autofocus,onFocus:this.handlePatternInputFocus,onBlur:this.handlePatternInputBlur,onInput:this.handlePatternInputInput,onCompositionstart:this.handleCompositionStart,onCompositionend:this.handleCompositionEnd})),O?d("div",{class:`${i}-base-selection-label__render-label ${i}-base-selection-overlay`,key:"input"},d("div",{class:`${i}-base-selection-overlay__wrapper`},c?c({option:this.selectedOption,handleClose:()=>{}}):v?v(this.selectedOption,!0):Te(this.label,this.selectedOption,!0))):null,F?d("div",{class:`${i}-base-selection-placeholder ${i}-base-selection-overlay`,key:"placeholder"},d("div",{class:`${i}-base-selection-overlay__wrapper`},this.filterablePlaceholder)):null,T)}else M=d("div",{ref:"singleElRef",class:`${i}-base-selection-label`,tabindex:this.disabled?void 0:0},this.label!==void 0?d("div",{class:`${i}-base-selection-input`,title:Rt(this.label),key:"input"},d("div",{class:`${i}-base-selection-input__content`},c?c({option:this.selectedOption,handleClose:()=>{}}):v?v(this.selectedOption,!0):Te(this.label,this.selectedOption,!0))):d("div",{class:`${i}-base-selection-placeholder ${i}-base-selection-overlay`,key:"placeholder"},d("div",{class:`${i}-base-selection-placeholder__inner`},this.placeholder)),T);return d("div",{ref:"selfRef",class:[`${i}-base-selection`,this.rtlEnabled&&`${i}-base-selection--rtl`,this.themeClass,e&&`${i}-base-selection--${e}-status`,{[`${i}-base-selection--active`]:this.active,[`${i}-base-selection--selected`]:this.selected||this.active&&this.pattern,[`${i}-base-selection--disabled`]:this.disabled,[`${i}-base-selection--multiple`]:this.multiple,[`${i}-base-selection--focus`]:this.focused}],style:this.cssVars,onClick:this.onClick,onMouseenter:this.handleMouseEnter,onMouseleave:this.handleMouseLeave,onKeydown:this.onKeydown,onFocusin:this.handleFocusin,onFocusout:this.handleFocusout,onMousedown:this.handleMouseDown},M,a?d("div",{class:`${i}-base-selection__border`}):null,a?d("div",{class:`${i}-base-selection__state-border`}):null)}});function Qe(e){return e.type==="group"}function Vt(e){return e.type==="ignored"}function dt(e,n){try{return!!(1+n.toString().toLowerCase().indexOf(e.trim().toLowerCase()))}catch{return!1}}function Wo(e,n){return{getIsGroup:Qe,getIgnored:Vt,getKey(r){return Qe(r)?r.name||r.key||"key-required":r[e]},getChildren(r){return r[n]}}}function jo(e,n,t,r){if(!n)return e;function o(s){if(!Array.isArray(s))return[];const a=[];for(const i of s)if(Qe(i)){const h=o(i[r]);h.length&&a.push(Object.assign({},i,{[r]:h}))}else{if(Vt(i))continue;n(t,i)&&a.push(i)}return a}return o(e)}function Uo(e,n,t){const r=new Map;return e.forEach(o=>{Qe(o)?o[t].forEach(s=>{r.set(s[n],s)}):r.set(o[n],o)}),r}const Go=ce([K("select",`
 z-index: auto;
 outline: none;
 width: 100%;
 position: relative;
 `),K("select-menu",`
 margin: 4px 0;
 box-shadow: var(--n-menu-box-shadow);
 `,[At({originalTransition:"background-color .3s var(--n-bezier), box-shadow .3s var(--n-bezier)"})])]),qo=Object.assign(Object.assign({},be.props),{to:ht.propTo,bordered:{type:Boolean,default:void 0},clearable:Boolean,clearFilterAfterSelect:{type:Boolean,default:!0},options:{type:Array,default:()=>[]},defaultValue:{type:[String,Number,Array],default:null},keyboard:{type:Boolean,default:!0},value:[String,Number,Array],placeholder:String,menuProps:Object,multiple:Boolean,size:String,filterable:Boolean,disabled:{type:Boolean,default:void 0},remote:Boolean,loading:Boolean,filter:Function,placement:{type:String,default:"bottom-start"},widthMode:{type:String,default:"trigger"},tag:Boolean,onCreate:Function,fallbackOption:{type:[Function,Boolean],default:void 0},show:{type:Boolean,default:void 0},showArrow:{type:Boolean,default:!0},maxTagCount:[Number,String],ellipsisTagPopoverProps:Object,consistentMenuWidth:{type:Boolean,default:!0},virtualScroll:{type:Boolean,default:!0},labelField:{type:String,default:"label"},valueField:{type:String,default:"value"},childrenField:{type:String,default:"children"},renderLabel:Function,renderOption:Function,renderTag:Function,"onUpdate:value":[Function,Array],inputProps:Object,nodeProps:Function,ignoreComposition:{type:Boolean,default:!0},showOnFocus:Boolean,onUpdateValue:[Function,Array],onBlur:[Function,Array],onClear:[Function,Array],onFocus:[Function,Array],onScroll:[Function,Array],onSearch:[Function,Array],onUpdateShow:[Function,Array],"onUpdate:show":[Function,Array],displayDirective:{type:String,default:"show"},resetMenuOnOptionsChange:{type:Boolean,default:!0},status:String,showCheckmark:{type:Boolean,default:!0},onChange:[Function,Array],items:Array}),Qo=ue({name:"Select",props:qo,setup(e){const{mergedClsPrefixRef:n,mergedBorderedRef:t,namespaceRef:r,inlineThemeDisabled:o}=Le(e),s=be("Select","-select",Go,Nn,e,n),a=_(e.defaultValue),i=ie(e,"value"),h=kt(i,a),b=_(!1),c=_(""),v=Wn(e,["items","options"]),S=_([]),y=_([]),g=E(()=>y.value.concat(S.value).concat(v.value)),T=E(()=>{const{filter:l}=e;if(l)return l;const{labelField:C,valueField:I}=e;return(V,D)=>{if(!D)return!1;const N=D[C];if(typeof N=="string")return dt(V,N);const H=D[I];return typeof H=="string"?dt(V,H):typeof H=="number"?dt(V,String(H)):!1}}),M=E(()=>{if(e.remote)return v.value;{const{value:l}=g,{value:C}=c;return!C.length||!e.filterable?l:jo(l,T.value,C,e.childrenField)}}),z=E(()=>{const{valueField:l,childrenField:C}=e,I=Wo(l,C);return Oo(M.value,I)}),F=E(()=>Uo(g.value,e.valueField,e.childrenField)),O=_(!1),p=kt(ie(e,"show"),O),m=_(null),$=_(null),A=_(null),{localeRef:j}=Lt("Select"),U=E(()=>{var l;return(l=e.placeholder)!==null&&l!==void 0?l:j.value.placeholder}),B=[],w=_(new Map),R=E(()=>{const{fallbackOption:l}=e;if(l===void 0){const{labelField:C,valueField:I}=e;return V=>({[C]:String(V),[I]:V})}return l===!1?!1:C=>Object.assign(l(C),{value:C})});function P(l){const C=e.remote,{value:I}=w,{value:V}=F,{value:D}=R,N=[];return l.forEach(H=>{if(V.has(H))N.push(V.get(H));else if(C&&I.has(H))N.push(I.get(H));else if(D){const oe=D(H);oe&&N.push(oe)}}),N}const W=E(()=>{if(e.multiple){const{value:l}=h;return Array.isArray(l)?P(l):[]}return null}),G=E(()=>{const{value:l}=h;return!e.multiple&&!Array.isArray(l)?l===null?null:P([l])[0]||null:null}),q=_n(e),{mergedSizeRef:Z,mergedDisabledRef:Y,mergedStatusRef:le}=q;function te(l,C){const{onChange:I,"onUpdate:value":V,onUpdateValue:D}=e,{nTriggerFormChange:N,nTriggerFormInput:H}=q;I&&ve(I,l,C),D&&ve(D,l,C),V&&ve(V,l,C),a.value=l,N(),H()}function re(l){const{onBlur:C}=e,{nTriggerFormBlur:I}=q;C&&ve(C,l),I()}function u(){const{onClear:l}=e;l&&ve(l)}function x(l){const{onFocus:C,showOnFocus:I}=e,{nTriggerFormFocus:V}=q;C&&ve(C,l),V(),I&&he()}function J(l){const{onSearch:C}=e;C&&ve(C,l)}function fe(l){const{onScroll:C}=e;C&&ve(C,l)}function pe(){var l;const{remote:C,multiple:I}=e;if(C){const{value:V}=w;if(I){const{valueField:D}=e;(l=W.value)===null||l===void 0||l.forEach(N=>{V.set(N[D],N)})}else{const D=G.value;D&&V.set(D[e.valueField],D)}}}function ye(l){const{onUpdateShow:C,"onUpdate:show":I}=e;C&&ve(C,l),I&&ve(I,l),O.value=l}function he(){Y.value||(ye(!0),O.value=!0,e.filterable&&Ve())}function ae(){ye(!1)}function Ce(){c.value="",y.value=B}const de=_(!1);function Fe(){e.filterable&&(de.value=!0)}function Ie(){e.filterable&&(de.value=!1,p.value||Ce())}function Me(){Y.value||(p.value?e.filterable?Ve():ae():he())}function Be(l){var C,I;!((I=(C=A.value)===null||C===void 0?void 0:C.selfRef)===null||I===void 0)&&I.contains(l.relatedTarget)||(b.value=!1,re(l),ae())}function xe(l){x(l),b.value=!0}function Se(){b.value=!0}function _e(l){var C;!((C=m.value)===null||C===void 0)&&C.$el.contains(l.relatedTarget)||(b.value=!1,re(l),ae())}function Ee(){var l;(l=m.value)===null||l===void 0||l.focus(),ae()}function $e(l){var C;p.value&&(!((C=m.value)===null||C===void 0)&&C.$el.contains(Ln(l))||ae())}function Pe(l){if(!Array.isArray(l))return[];if(R.value)return Array.from(l);{const{remote:C}=e,{value:I}=F;if(C){const{value:V}=w;return l.filter(D=>I.has(D)||V.has(D))}else return l.filter(V=>I.has(V))}}function ke(l){se(l.rawNode)}function se(l){if(Y.value)return;const{tag:C,remote:I,clearFilterAfterSelect:V,valueField:D}=e;if(C&&!I){const{value:N}=y,H=N[0]||null;if(H){const oe=S.value;oe.length?oe.push(H):S.value=[H],y.value=B}}if(I&&w.value.set(l[D],l),e.multiple){const N=Pe(h.value),H=N.findIndex(oe=>oe===l[D]);if(~H){if(N.splice(H,1),C&&!I){const oe=f(l[D]);~oe&&(S.value.splice(oe,1),V&&(c.value=""))}}else N.push(l[D]),V&&(c.value="");te(N,P(N))}else{if(C&&!I){const N=f(l[D]);~N?S.value=[S.value[N]]:S.value=B}Ke(),ae(),te(l[D],l)}}function f(l){return S.value.findIndex(I=>I[e.valueField]===l)}function k(l){p.value||he();const{value:C}=l.target;c.value=C;const{tag:I,remote:V}=e;if(J(C),I&&!V){if(!C){y.value=B;return}const{onCreate:D}=e,N=D?D(C):{[e.labelField]:C,[e.valueField]:C},{valueField:H,labelField:oe}=e;v.value.some(ge=>ge[H]===N[H]||ge[oe]===N[oe])||S.value.some(ge=>ge[H]===N[H]||ge[oe]===N[oe])?y.value=B:y.value=[N]}}function ne(l){l.stopPropagation();const{multiple:C}=e;!C&&e.filterable&&ae(),u(),C?te([],[]):te(null,null)}function et(l){!Ae(l,"action")&&!Ae(l,"empty")&&!Ae(l,"header")&&l.preventDefault()}function tt(l){fe(l)}function He(l){var C,I,V,D,N;if(!e.keyboard){l.preventDefault();return}switch(l.key){case" ":if(e.filterable)break;l.preventDefault();case"Enter":if(!(!((C=m.value)===null||C===void 0)&&C.isComposing)){if(p.value){const H=(I=A.value)===null||I===void 0?void 0:I.getPendingTmNode();H?ke(H):e.filterable||(ae(),Ke())}else if(he(),e.tag&&de.value){const H=y.value[0];if(H){const oe=H[e.valueField],{value:ge}=h;e.multiple&&Array.isArray(ge)&&ge.includes(oe)||se(H)}}}l.preventDefault();break;case"ArrowUp":if(l.preventDefault(),e.loading)return;p.value&&((V=A.value)===null||V===void 0||V.prev());break;case"ArrowDown":if(l.preventDefault(),e.loading)return;p.value?(D=A.value)===null||D===void 0||D.next():he();break;case"Escape":p.value&&(Dn(l),ae()),(N=m.value)===null||N===void 0||N.focus();break}}function Ke(){var l;(l=m.value)===null||l===void 0||l.focus()}function Ve(){var l;(l=m.value)===null||l===void 0||l.focusInput()}function nt(){var l;p.value&&((l=$.value)===null||l===void 0||l.syncPosition())}pe(),Oe(ie(e,"options"),pe);const ot={focus:()=>{var l;(l=m.value)===null||l===void 0||l.focus()},focusInput:()=>{var l;(l=m.value)===null||l===void 0||l.focusInput()},blur:()=>{var l;(l=m.value)===null||l===void 0||l.blur()},blurInput:()=>{var l;(l=m.value)===null||l===void 0||l.blurInput()}},We=E(()=>{const{self:{menuBoxShadow:l}}=s.value;return{"--n-menu-box-shadow":l}}),we=o?De("select",void 0,We,e):void 0;return Object.assign(Object.assign({},ot),{mergedStatus:le,mergedClsPrefix:n,mergedBordered:t,namespace:r,treeMate:z,isMounted:En(),triggerRef:m,menuRef:A,pattern:c,uncontrolledShow:O,mergedShow:p,adjustedTo:ht(e),uncontrolledValue:a,mergedValue:h,followerRef:$,localizedPlaceholder:U,selectedOption:G,selectedOptions:W,mergedSize:Z,mergedDisabled:Y,focused:b,activeWithoutMenuOpen:de,inlineThemeDisabled:o,onTriggerInputFocus:Fe,onTriggerInputBlur:Ie,handleTriggerOrMenuResize:nt,handleMenuFocus:Se,handleMenuBlur:_e,handleMenuTabOut:Ee,handleTriggerClick:Me,handleToggle:ke,handleDeleteOption:se,handlePatternInput:k,handleClear:ne,handleTriggerBlur:Be,handleTriggerFocus:xe,handleKeydown:He,handleMenuAfterLeave:Ce,handleMenuClickOutside:$e,handleMenuScroll:tt,handleMenuKeydown:He,handleMenuMousedown:et,mergedTheme:s,cssVars:o?void 0:We,themeClass:we==null?void 0:we.themeClass,onRender:we==null?void 0:we.onRender})},render(){return d("div",{class:`${this.mergedClsPrefix}-select`},d(jn,null,{default:()=>[d(Un,null,{default:()=>d(Vo,{ref:"triggerRef",inlineThemeDisabled:this.inlineThemeDisabled,status:this.mergedStatus,inputProps:this.inputProps,clsPrefix:this.mergedClsPrefix,showArrow:this.showArrow,maxTagCount:this.maxTagCount,ellipsisTagPopoverProps:this.ellipsisTagPopoverProps,bordered:this.mergedBordered,active:this.activeWithoutMenuOpen||this.mergedShow,pattern:this.pattern,placeholder:this.localizedPlaceholder,selectedOption:this.selectedOption,selectedOptions:this.selectedOptions,multiple:this.multiple,renderTag:this.renderTag,renderLabel:this.renderLabel,filterable:this.filterable,clearable:this.clearable,disabled:this.mergedDisabled,size:this.mergedSize,theme:this.mergedTheme.peers.InternalSelection,labelField:this.labelField,valueField:this.valueField,themeOverrides:this.mergedTheme.peerOverrides.InternalSelection,loading:this.loading,focused:this.focused,onClick:this.handleTriggerClick,onDeleteOption:this.handleDeleteOption,onPatternInput:this.handlePatternInput,onClear:this.handleClear,onBlur:this.handleTriggerBlur,onFocus:this.handleTriggerFocus,onKeydown:this.handleKeydown,onPatternBlur:this.onTriggerInputBlur,onPatternFocus:this.onTriggerInputFocus,onResize:this.handleTriggerOrMenuResize,ignoreComposition:this.ignoreComposition},{arrow:()=>{var e,n;return[(n=(e=this.$slots).arrow)===null||n===void 0?void 0:n.call(e)]}})}),d(Gn,{ref:"followerRef",show:this.mergedShow,to:this.adjustedTo,teleportDisabled:this.adjustedTo===ht.tdkey,containerClass:this.namespace,width:this.consistentMenuWidth?"target":void 0,minWidth:"target",placement:this.placement},{default:()=>d($t,{name:"fade-in-scale-up-transition",appear:this.isMounted,onAfterLeave:this.handleMenuAfterLeave},{default:()=>{var e,n,t;return this.mergedShow||this.displayDirective==="show"?((e=this.onRender)===null||e===void 0||e.call(this),$n(d(_o,Object.assign({},this.menuProps,{ref:"menuRef",onResize:this.handleTriggerOrMenuResize,inlineThemeDisabled:this.inlineThemeDisabled,virtualScroll:this.consistentMenuWidth&&this.virtualScroll,class:[`${this.mergedClsPrefix}-select-menu`,this.themeClass,(n=this.menuProps)===null||n===void 0?void 0:n.class],clsPrefix:this.mergedClsPrefix,focusable:!0,labelField:this.labelField,valueField:this.valueField,autoPending:!0,nodeProps:this.nodeProps,theme:this.mergedTheme.peers.InternalSelectMenu,themeOverrides:this.mergedTheme.peerOverrides.InternalSelectMenu,treeMate:this.treeMate,multiple:this.multiple,size:"medium",renderOption:this.renderOption,renderLabel:this.renderLabel,value:this.mergedValue,style:[(t=this.menuProps)===null||t===void 0?void 0:t.style,this.cssVars],onToggle:this.handleToggle,onScroll:this.handleMenuScroll,onFocus:this.handleMenuFocus,onBlur:this.handleMenuBlur,onKeydown:this.handleMenuKeydown,onTabOut:this.handleMenuTabOut,onMousedown:this.handleMenuMousedown,show:this.mergedShow,showCheckmark:this.showCheckmark,resetMenuOnOptionsChange:this.resetMenuOnOptionsChange}),{empty:()=>{var r,o;return[(o=(r=this.$slots).empty)===null||o===void 0?void 0:o.call(r)]},header:()=>{var r,o;return[(o=(r=this.$slots).header)===null||o===void 0?void 0:o.call(r)]},action:()=>{var r,o;return[(o=(r=this.$slots).action)===null||o===void 0?void 0:o.call(r)]}}),this.displayDirective==="show"?[[An,this.mergedShow],[St,this.handleMenuClickOutside,void 0,{capture:!0}]]:[[St,this.handleMenuClickOutside,void 0,{capture:!0}]])):null}})})]}))}});export{no as F,_o as N,Jn as V,Qo as _,Wo as a,Io as b,Oo as c,ct as d,Ae as h,rt as m,Ho as t};
