import{n as k,a7 as $,t as c,X as l,v as S,x as R,A as y,z as P,B as I,I as p,F as L,cc as M,cd as O,bA as T,aE as A,a8 as D,ce as V,L as F,bd as W,ac as B}from"./index-c0918e59.js";function H(e,t="default",a=[]){const n=e.$slots[t];return n===void 0?a:n()}const N=k("divider",`
 position: relative;
 display: flex;
 width: 100%;
 box-sizing: border-box;
 font-size: 16px;
 color: var(--n-text-color);
 transition:
 color .3s var(--n-bezier),
 background-color .3s var(--n-bezier);
`,[$("vertical",`
 margin-top: 24px;
 margin-bottom: 24px;
 `,[$("no-title",`
 display: flex;
 align-items: center;
 `)]),c("title",`
 display: flex;
 align-items: center;
 margin-left: 12px;
 margin-right: 12px;
 white-space: nowrap;
 font-weight: var(--n-font-weight);
 `),l("title-position-left",[c("line",[l("left",{width:"28px"})])]),l("title-position-right",[c("line",[l("right",{width:"28px"})])]),l("dashed",[c("line",`
 background-color: #0000;
 height: 0px;
 width: 100%;
 border-style: dashed;
 border-width: 1px 0 0;
 `)]),l("vertical",`
 display: inline-block;
 height: 1em;
 margin: 0 8px;
 vertical-align: middle;
 width: 1px;
 `),c("line",`
 border: none;
 transition: background-color .3s var(--n-bezier), border-color .3s var(--n-bezier);
 height: 1px;
 width: 100%;
 margin: 0;
 `),$("dashed",[c("line",{backgroundColor:"var(--n-color)"})]),l("dashed",[c("line",{borderColor:"var(--n-color)"})]),l("vertical",{backgroundColor:"var(--n-color)"})]),U=Object.assign(Object.assign({},y.props),{titlePlacement:{type:String,default:"center"},dashed:Boolean,vertical:Boolean}),Z=S({name:"Divider",props:U,setup(e){const{mergedClsPrefixRef:t,inlineThemeDisabled:a}=R(e),s=y("Divider","-divider",N,M,e,t),n=P(()=>{const{common:{cubicBezierEaseInOut:r},self:{color:o,textColor:m,fontWeight:v}}=s.value;return{"--n-bezier":r,"--n-color":o,"--n-text-color":m,"--n-font-weight":v}}),i=a?I("divider",void 0,n,e):void 0;return{mergedClsPrefix:t,cssVars:a?void 0:n,themeClass:i==null?void 0:i.themeClass,onRender:i==null?void 0:i.onRender}},render(){var e;const{$slots:t,titlePlacement:a,vertical:s,dashed:n,cssVars:i,mergedClsPrefix:r}=this;return(e=this.onRender)===null||e===void 0||e.call(this),p("div",{role:"separator",class:[`${r}-divider`,this.themeClass,{[`${r}-divider--vertical`]:s,[`${r}-divider--no-title`]:!t.default,[`${r}-divider--dashed`]:n,[`${r}-divider--title-position-${a}`]:t.default&&a}],style:i},s?null:p("div",{class:`${r}-divider__line ${r}-divider__line--left`}),!s&&t.default?p(L,null,p("div",{class:`${r}-divider__title`},this.$slots),p("div",{class:`${r}-divider__line ${r}-divider__line--right`})):null)}});function J(){return O}const K={name:"Space",self:J},X=K;let C;function q(){if(!T)return!0;if(C===void 0){const e=document.createElement("div");e.style.display="flex",e.style.flexDirection="column",e.style.rowGap="1px",e.appendChild(document.createElement("div")),e.appendChild(document.createElement("div")),document.body.appendChild(e);const t=e.scrollHeight===1;return document.body.removeChild(e),C=t}return C}const Q=Object.assign(Object.assign({},y.props),{align:String,justify:{type:String,default:"start"},inline:Boolean,vertical:Boolean,reverse:Boolean,size:{type:[String,Number,Array],default:"medium"},wrapItem:{type:Boolean,default:!0},itemClass:String,itemStyle:[String,Object],wrap:{type:Boolean,default:!0},internalUseGap:{type:Boolean,default:void 0}}),ee=S({name:"Space",props:Q,setup(e){const{mergedClsPrefixRef:t,mergedRtlRef:a}=R(e),s=y("Space","-space",void 0,X,e,t),n=A("Space",a,t);return{useGap:q(),rtlEnabled:n,mergedClsPrefix:t,margin:P(()=>{const{size:i}=e;if(Array.isArray(i))return{horizontal:i[0],vertical:i[1]};if(typeof i=="number")return{horizontal:i,vertical:i};const{self:{[F("gap",i)]:r}}=s.value,{row:o,col:m}=W(r);return{horizontal:B(m),vertical:B(o)}})}},render(){const{vertical:e,reverse:t,align:a,inline:s,justify:n,itemClass:i,itemStyle:r,margin:o,wrap:m,mergedClsPrefix:v,rtlEnabled:_,useGap:g,wrapItem:E,internalUseGap:j}=this,h=D(H(this),!1);if(!h.length)return null;const z=`${o.horizontal}px`,x=`${o.horizontal/2}px`,G=`${o.vertical}px`,u=`${o.vertical/2}px`,f=h.length-1,b=n.startsWith("space-");return p("div",{role:"none",class:[`${v}-space`,_&&`${v}-space--rtl`],style:{display:s?"inline-flex":"flex",flexDirection:(()=>e&&!t?"column":e&&t?"column-reverse":!e&&t?"row-reverse":"row")(),justifyContent:["start","end"].includes(n)?`flex-${n}`:n,flexWrap:!m||e?"nowrap":"wrap",marginTop:g||e?"":`-${u}`,marginBottom:g||e?"":`-${u}`,alignItems:a,gap:g?`${o.vertical}px ${o.horizontal}px`:""}},!E&&(g||j)?h:h.map((w,d)=>w.type===V?w:p("div",{role:"none",class:i,style:[r,{maxWidth:"100%"},g?"":e?{marginBottom:d!==f?G:""}:_?{marginLeft:b?n==="space-between"&&d===f?"":x:d!==f?z:"",marginRight:b?n==="space-between"&&d===0?"":x:"",paddingTop:u,paddingBottom:u}:{marginRight:b?n==="space-between"&&d===f?"":x:d!==f?z:"",marginLeft:b?n==="space-between"&&d===0?"":x:"",paddingTop:u,paddingBottom:u}]},w)))}});export{ee as _,Z as a,H as g};
