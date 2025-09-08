import{n as g,X as l,q as d,A as h,v,x as b,br as y,z as a,B as C,aY as z,I as m,a2 as _}from"./index-c0918e59.js";import{f as $}from"./TopWitge-0056793e.js";const x=g("icon",`
 height: 1em;
 width: 1em;
 line-height: 1em;
 text-align: center;
 display: inline-block;
 position: relative;
 fill: currentColor;
`,[l("color-transition",{transition:"color .3s var(--n-bezier)"}),l("depth",{color:"var(--n-color)"},[d("svg",{opacity:"var(--n-opacity)",transition:"opacity .3s var(--n-bezier)"})]),d("svg",{height:"1em",width:"1em"})]),R=Object.assign(Object.assign({},h.props),{depth:[String,Number],size:[Number,String],color:String,component:[Object,Function]}),P=v({_n_icon__:!0,name:"Icon",inheritAttrs:!1,props:R,setup(e){const{mergedClsPrefixRef:o,inlineThemeDisabled:t}=b(e),s=h("Icon","-icon",x,y,e,o),r=a(()=>{const{depth:i}=e,{common:{cubicBezierEaseInOut:c},self:u}=s.value;if(i!==void 0){const{color:p,[`opacity${i}Depth`]:f}=u;return{"--n-bezier":c,"--n-color":p,"--n-opacity":f}}return{"--n-bezier":c,"--n-color":"","--n-opacity":""}}),n=t?C("icon",a(()=>`${e.depth||"d"}`),r,e):void 0;return{mergedClsPrefix:o,mergedStyle:a(()=>{const{size:i,color:c}=e;return{fontSize:$(i),color:c}}),cssVars:t?void 0:r,themeClass:n==null?void 0:n.themeClass,onRender:n==null?void 0:n.onRender}},render(){var e;const{$parent:o,depth:t,mergedClsPrefix:s,component:r,onRender:n,themeClass:i}=this;return!((e=o==null?void 0:o.$options)===null||e===void 0)&&e._n_icon__&&z("icon","don't wrap `n-icon` inside `n-icon`"),n==null||n(),m("i",_(this.$attrs,{role:"img",class:[`${s}-icon`,i,{[`${s}-icon--depth`]:t,[`${s}-icon--color-transition`]:t!==void 0}],style:[this.cssVars,this.mergedStyle]}),r?m(r):this.$slots)}});export{P as N};
