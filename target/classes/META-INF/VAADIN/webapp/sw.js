!function(e){var t={};function s(n){if(t[n])return t[n].exports;var r=t[n]={i:n,l:!1,exports:{}};return e[n].call(r.exports,r,r.exports,s),r.l=!0,r.exports}s.m=e,s.c=t,s.d=function(e,t,n){s.o(e,t)||Object.defineProperty(e,t,{enumerable:!0,get:n})},s.r=function(e){"undefined"!=typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})},s.t=function(e,t){if(1&t&&(e=s(e)),8&t)return e;if(4&t&&"object"==typeof e&&e&&e.__esModule)return e;var n=Object.create(null);if(s.r(n),Object.defineProperty(n,"default",{enumerable:!0,value:e}),2&t&&"string"!=typeof e)for(var r in e)s.d(n,r,function(t){return e[t]}.bind(null,r));return n},s.n=function(e){var t=e&&e.__esModule?function(){return e.default}:function(){return e};return s.d(t,"a",t),t},s.o=function(e,t){return Object.prototype.hasOwnProperty.call(e,t)},s.p="",s(s.s=4)}([function(e,t,s){"use strict";try{self["workbox:core:6.4.2"]&&_()}catch(e){}},function(e,t,s){"use strict";try{self["workbox:precaching:6.4.2"]&&_()}catch(e){}},function(e,t,s){"use strict";try{self["workbox:routing:6.4.2"]&&_()}catch(e){}},function(e,t,s){"use strict";try{self["workbox:strategies:6.4.2"]&&_()}catch(e){}},function(e,t,s){"use strict";s.r(t);s(0);const n=(e,...t)=>{let s=e;return t.length>0&&(s+=" :: "+JSON.stringify(t)),s};class r extends Error{constructor(e,t){super(n(e,t)),this.name=e,this.details=t}}const a=new Set;const i={googleAnalytics:"googleAnalytics",precache:"precache-v2",prefix:"workbox",runtime:"runtime",suffix:"undefined"!=typeof registration?registration.scope:""},o=e=>[i.prefix,e,i.suffix].filter(e=>e&&e.length>0).join("-"),c=e=>e||o(i.precache),h=e=>e||o(i.runtime);function l(e,t){const s=new URL(e);for(const e of t)s.searchParams.delete(e);return s.href}let u;class d{constructor(){this.promise=new Promise((e,t)=>{this.resolve=e,this.reject=t})}}const f=e=>new URL(String(e),location.href).href.replace(new RegExp("^"+location.origin),"");function p(e){return new Promise(t=>setTimeout(t,e))}function w(e,t){const s=t();return e.waitUntil(s),s}async function g(e,t){let s=null;if(e.url){s=new URL(e.url).origin}if(s!==self.location.origin)throw new r("cross-origin-copy-response",{origin:s});const n=e.clone(),a={headers:new Headers(n.headers),status:n.status,statusText:n.statusText},i=t?t(a):a,o=function(){if(void 0===u){const e=new Response("");if("body"in e)try{new Response(e.body),u=!0}catch(e){u=!1}u=!1}return u}()?n.body:await n.blob();return new Response(o,i)}s(1);function y(e){if(!e)throw new r("add-to-cache-list-unexpected-type",{entry:e});if("string"==typeof e){const t=new URL(e,location.href);return{cacheKey:t.href,url:t.href}}const{revision:t,url:s}=e;if(!s)throw new r("add-to-cache-list-unexpected-type",{entry:e});if(!t){const e=new URL(s,location.href);return{cacheKey:e.href,url:e.href}}const n=new URL(s,location.href),a=new URL(s,location.href);return n.searchParams.set("__WB_REVISION__",t),{cacheKey:n.href,url:a.href}}class m{constructor(){this.updatedURLs=[],this.notUpdatedURLs=[],this.handlerWillStart=async({request:e,state:t})=>{t&&(t.originalRequest=e)},this.cachedResponseWillBeUsed=async({event:e,state:t,cachedResponse:s})=>{if("install"===e.type&&t&&t.originalRequest&&t.originalRequest instanceof Request){const e=t.originalRequest.url;s?this.notUpdatedURLs.push(e):this.updatedURLs.push(e)}return s}}}class _{constructor({precacheController:e}){this.cacheKeyWillBeUsed=async({request:e,params:t})=>{const s=(null==t?void 0:t.cacheKey)||this._precacheController.getCacheKeyForURL(e.url);return s?new Request(s,{headers:e.headers}):e},this._precacheController=e}}s(3);function v(e){return"string"==typeof e?new Request(e):e}class R{constructor(e,t){this._cacheKeys={},Object.assign(this,t),this.event=t.event,this._strategy=e,this._handlerDeferred=new d,this._extendLifetimePromises=[],this._plugins=[...e.plugins],this._pluginStateMap=new Map;for(const e of this._plugins)this._pluginStateMap.set(e,{});this.event.waitUntil(this._handlerDeferred.promise)}async fetch(e){const{event:t}=this;let s=v(e);if("navigate"===s.mode&&t instanceof FetchEvent&&t.preloadResponse){const e=await t.preloadResponse;if(e)return e}const n=this.hasCallback("fetchDidFail")?s.clone():null;try{for(const e of this.iterateCallbacks("requestWillFetch"))s=await e({request:s.clone(),event:t})}catch(e){if(e instanceof Error)throw new r("plugin-error-request-will-fetch",{thrownErrorMessage:e.message})}const a=s.clone();try{let e;e=await fetch(s,"navigate"===s.mode?void 0:this._strategy.fetchOptions);for(const s of this.iterateCallbacks("fetchDidSucceed"))e=await s({event:t,request:a,response:e});return e}catch(e){throw n&&await this.runCallbacks("fetchDidFail",{error:e,event:t,originalRequest:n.clone(),request:a.clone()}),e}}async fetchAndCachePut(e){const t=await this.fetch(e),s=t.clone();return this.waitUntil(this.cachePut(e,s)),t}async cacheMatch(e){const t=v(e);let s;const{cacheName:n,matchOptions:r}=this._strategy,a=await this.getCacheKey(t,"read"),i=Object.assign(Object.assign({},r),{cacheName:n});s=await caches.match(a,i);for(const e of this.iterateCallbacks("cachedResponseWillBeUsed"))s=await e({cacheName:n,matchOptions:r,cachedResponse:s,request:a,event:this.event})||void 0;return s}async cachePut(e,t){const s=v(e);await p(0);const n=await this.getCacheKey(s,"write");if(!t)throw new r("cache-put-with-no-response",{url:f(n.url)});const i=await this._ensureResponseSafeToCache(t);if(!i)return!1;const{cacheName:o,matchOptions:c}=this._strategy,h=await self.caches.open(o),u=this.hasCallback("cacheDidUpdate"),d=u?await async function(e,t,s,n){const r=l(t.url,s);if(t.url===r)return e.match(t,n);const a=Object.assign(Object.assign({},n),{ignoreSearch:!0}),i=await e.keys(t,a);for(const t of i){if(r===l(t.url,s))return e.match(t,n)}}(h,n.clone(),["__WB_REVISION__"],c):null;try{await h.put(n,u?i.clone():i)}catch(e){if(e instanceof Error)throw"QuotaExceededError"===e.name&&await async function(){for(const e of a)await e()}(),e}for(const e of this.iterateCallbacks("cacheDidUpdate"))await e({cacheName:o,oldResponse:d,newResponse:i.clone(),request:n,event:this.event});return!0}async getCacheKey(e,t){const s=`${e.url} | ${t}`;if(!this._cacheKeys[s]){let n=e;for(const e of this.iterateCallbacks("cacheKeyWillBeUsed"))n=v(await e({mode:t,request:n,event:this.event,params:this.params}));this._cacheKeys[s]=n}return this._cacheKeys[s]}hasCallback(e){for(const t of this._strategy.plugins)if(e in t)return!0;return!1}async runCallbacks(e,t){for(const s of this.iterateCallbacks(e))await s(t)}*iterateCallbacks(e){for(const t of this._strategy.plugins)if("function"==typeof t[e]){const s=this._pluginStateMap.get(t),n=n=>{const r=Object.assign(Object.assign({},n),{state:s});return t[e](r)};yield n}}waitUntil(e){return this._extendLifetimePromises.push(e),e}async doneWaiting(){let e;for(;e=this._extendLifetimePromises.shift();)await e}destroy(){this._handlerDeferred.resolve(null)}async _ensureResponseSafeToCache(e){let t=e,s=!1;for(const e of this.iterateCallbacks("cacheWillUpdate"))if(t=await e({request:this.request,response:t,event:this.event})||void 0,s=!0,!t)break;return s||t&&200!==t.status&&(t=void 0),t}}class b{constructor(e={}){this.cacheName=h(e.cacheName),this.plugins=e.plugins||[],this.fetchOptions=e.fetchOptions,this.matchOptions=e.matchOptions}handle(e){const[t]=this.handleAll(e);return t}handleAll(e){e instanceof FetchEvent&&(e={event:e,request:e.request});const t=e.event,s="string"==typeof e.request?new Request(e.request):e.request,n="params"in e?e.params:void 0,r=new R(this,{event:t,request:s,params:n}),a=this._getResponse(r,s,t);return[a,this._awaitComplete(a,r,s,t)]}async _getResponse(e,t,s){await e.runCallbacks("handlerWillStart",{event:s,request:t});let n=void 0;try{if(n=await this._handle(t,e),!n||"error"===n.type)throw new r("no-response",{url:t.url})}catch(r){if(r instanceof Error)for(const a of e.iterateCallbacks("handlerDidError"))if(n=await a({error:r,event:s,request:t}),n)break;if(!n)throw r}for(const r of e.iterateCallbacks("handlerWillRespond"))n=await r({event:s,request:t,response:n});return n}async _awaitComplete(e,t,s,n){let r,a;try{r=await e}catch(a){}try{await t.runCallbacks("handlerDidRespond",{event:n,request:s,response:r}),await t.doneWaiting()}catch(e){e instanceof Error&&(a=e)}if(await t.runCallbacks("handlerDidComplete",{event:n,request:s,response:r,error:a}),t.destroy(),a)throw a}}class C extends b{constructor(e={}){e.cacheName=c(e.cacheName),super(e),this._fallbackToNetwork=!1!==e.fallbackToNetwork,this.plugins.push(C.copyRedirectedCacheableResponsesPlugin)}async _handle(e,t){const s=await t.cacheMatch(e);return s||(t.event&&"install"===t.event.type?await this._handleInstall(e,t):await this._handleFetch(e,t))}async _handleFetch(e,t){let s;const n=t.params||{};if(!this._fallbackToNetwork)throw new r("missing-precache-entry",{cacheName:this.cacheName,url:e.url});{0;const r=n.integrity,a=e.integrity,i=!a||a===r;if(s=await t.fetch(new Request(e,{integrity:a||r})),r&&i){this._useDefaultCacheabilityPluginIfNeeded();await t.cachePut(e,s.clone());0}}return s}async _handleInstall(e,t){this._useDefaultCacheabilityPluginIfNeeded();const s=await t.fetch(e);if(!await t.cachePut(e,s.clone()))throw new r("bad-precaching-response",{url:e.url,status:s.status});return s}_useDefaultCacheabilityPluginIfNeeded(){let e=null,t=0;for(const[s,n]of this.plugins.entries())n!==C.copyRedirectedCacheableResponsesPlugin&&(n===C.defaultPrecacheCacheabilityPlugin&&(e=s),n.cacheWillUpdate&&t++);0===t?this.plugins.push(C.defaultPrecacheCacheabilityPlugin):t>1&&null!==e&&this.plugins.splice(e,1)}}C.defaultPrecacheCacheabilityPlugin={cacheWillUpdate:async({response:e})=>!e||e.status>=400?null:e},C.copyRedirectedCacheableResponsesPlugin={cacheWillUpdate:async({response:e})=>e.redirected?await g(e):e};class q{constructor({cacheName:e,plugins:t=[],fallbackToNetwork:s=!0}={}){this._urlsToCacheKeys=new Map,this._urlsToCacheModes=new Map,this._cacheKeysToIntegrities=new Map,this._strategy=new C({cacheName:c(e),plugins:[...t,new _({precacheController:this})],fallbackToNetwork:s}),this.install=this.install.bind(this),this.activate=this.activate.bind(this)}get strategy(){return this._strategy}precache(e){this.addToCacheList(e),this._installAndActiveListenersAdded||(self.addEventListener("install",this.install),self.addEventListener("activate",this.activate),this._installAndActiveListenersAdded=!0)}addToCacheList(e){const t=[];for(const s of e){"string"==typeof s?t.push(s):s&&void 0===s.revision&&t.push(s.url);const{cacheKey:e,url:n}=y(s),a="string"!=typeof s&&s.revision?"reload":"default";if(this._urlsToCacheKeys.has(n)&&this._urlsToCacheKeys.get(n)!==e)throw new r("add-to-cache-list-conflicting-entries",{firstEntry:this._urlsToCacheKeys.get(n),secondEntry:e});if("string"!=typeof s&&s.integrity){if(this._cacheKeysToIntegrities.has(e)&&this._cacheKeysToIntegrities.get(e)!==s.integrity)throw new r("add-to-cache-list-conflicting-integrities",{url:n});this._cacheKeysToIntegrities.set(e,s.integrity)}if(this._urlsToCacheKeys.set(n,e),this._urlsToCacheModes.set(n,a),t.length>0){const e=`Workbox is precaching URLs without revision info: ${t.join(", ")}\nThis is generally NOT safe. Learn more at https://bit.ly/wb-precache`;console.warn(e)}}}install(e){return w(e,async()=>{const t=new m;this.strategy.plugins.push(t);for(const[t,s]of this._urlsToCacheKeys){const n=this._cacheKeysToIntegrities.get(s),r=this._urlsToCacheModes.get(t),a=new Request(t,{integrity:n,cache:r,credentials:"same-origin"});await Promise.all(this.strategy.handleAll({params:{cacheKey:s},request:a,event:e}))}const{updatedURLs:s,notUpdatedURLs:n}=t;return{updatedURLs:s,notUpdatedURLs:n}})}activate(e){return w(e,async()=>{const e=await self.caches.open(this.strategy.cacheName),t=await e.keys(),s=new Set(this._urlsToCacheKeys.values()),n=[];for(const r of t)s.has(r.url)||(await e.delete(r),n.push(r.url));return{deletedURLs:n}})}getURLsToCacheKeys(){return this._urlsToCacheKeys}getCachedURLs(){return[...this._urlsToCacheKeys.keys()]}getCacheKeyForURL(e){const t=new URL(e,location.href);return this._urlsToCacheKeys.get(t.href)}getIntegrityForCacheKey(e){return this._cacheKeysToIntegrities.get(e)}async matchPrecache(e){const t=e instanceof Request?e.url:e,s=this.getCacheKeyForURL(t);if(s){return(await self.caches.open(this.strategy.cacheName)).match(s)}}createHandlerBoundToURL(e){const t=this.getCacheKeyForURL(e);if(!t)throw new r("non-precached-url",{url:e});return s=>(s.request=new Request(e),s.params=Object.assign({cacheKey:t},s.params),this.strategy.handle(s))}}let U;const k=()=>(U||(U=new q),U);s(2);const L=e=>e&&"object"==typeof e?e:{handle:e};class T{constructor(e,t,s="GET"){this.handler=L(t),this.match=e,this.method=s}setCatchHandler(e){this.catchHandler=L(e)}}class P extends T{constructor(e,t,s){super(({url:t})=>{const s=e.exec(t.href);if(s&&(t.origin===location.origin||0===s.index))return s.slice(1)},t,s)}}class K{constructor(){this._routes=new Map,this._defaultHandlerMap=new Map}get routes(){return this._routes}addFetchListener(){self.addEventListener("fetch",e=>{const{request:t}=e,s=this.handleRequest({request:t,event:e});s&&e.respondWith(s)})}addCacheListener(){self.addEventListener("message",e=>{if(e.data&&"CACHE_URLS"===e.data.type){const{payload:t}=e.data;0;const s=Promise.all(t.urlsToCache.map(t=>{"string"==typeof t&&(t=[t]);const s=new Request(...t);return this.handleRequest({request:s,event:e})}));e.waitUntil(s),e.ports&&e.ports[0]&&s.then(()=>e.ports[0].postMessage(!0))}})}handleRequest({request:e,event:t}){const s=new URL(e.url,location.href);if(!s.protocol.startsWith("http"))return void 0;const n=s.origin===location.origin,{params:r,route:a}=this.findMatchingRoute({event:t,request:e,sameOrigin:n,url:s});let i=a&&a.handler;const o=e.method;if(!i&&this._defaultHandlerMap.has(o)&&(i=this._defaultHandlerMap.get(o)),!i)return void 0;let c;try{c=i.handle({url:s,request:e,event:t,params:r})}catch(e){c=Promise.reject(e)}const h=a&&a.catchHandler;return c instanceof Promise&&(this._catchHandler||h)&&(c=c.catch(async n=>{if(h){0;try{return await h.handle({url:s,request:e,event:t,params:r})}catch(e){e instanceof Error&&(n=e)}}if(this._catchHandler)return this._catchHandler.handle({url:s,request:e,event:t});throw n})),c}findMatchingRoute({url:e,sameOrigin:t,request:s,event:n}){const r=this._routes.get(s.method)||[];for(const a of r){let r;const i=a.match({url:e,sameOrigin:t,request:s,event:n});if(i)return r=i,(Array.isArray(r)&&0===r.length||i.constructor===Object&&0===Object.keys(i).length||"boolean"==typeof i)&&(r=void 0),{route:a,params:r}}return{}}setDefaultHandler(e,t="GET"){this._defaultHandlerMap.set(t,L(e))}setCatchHandler(e){this._catchHandler=L(e)}registerRoute(e){this._routes.has(e.method)||this._routes.set(e.method,[]),this._routes.get(e.method).push(e)}unregisterRoute(e){if(!this._routes.has(e.method))throw new r("unregister-route-but-not-found-with-method",{method:e.method});const t=this._routes.get(e.method).indexOf(e);if(!(t>-1))throw new r("unregister-route-route-not-registered");this._routes.get(e.method).splice(t,1)}}let x;const M=()=>(x||(x=new K,x.addFetchListener(),x.addCacheListener()),x);function S(e,t,s){let n;if("string"==typeof e){const r=new URL(e,location.href);0;n=new T(({url:e})=>e.href===r.href,t,s)}else if(e instanceof RegExp)n=new P(e,t,s);else if("function"==typeof e)n=new T(e,t,s);else{if(!(e instanceof T))throw new r("unsupported-route-type",{moduleName:"workbox-routing",funcName:"registerRoute",paramName:"capture"});n=e}return M().registerRoute(n),n}class E extends T{constructor(e,t){super(({request:s})=>{const n=e.getURLsToCacheKeys();for(const r of function*(e,{ignoreURLParametersMatching:t=[/^utm_/,/^fbclid$/],directoryIndex:s="index.html",cleanURLs:n=!0,urlManipulation:r}={}){const a=new URL(e,location.href);a.hash="",yield a.href;const i=function(e,t=[]){for(const s of[...e.searchParams.keys()])t.some(e=>e.test(s))&&e.searchParams.delete(s);return e}(a,t);if(yield i.href,s&&i.pathname.endsWith("/")){const e=new URL(i.href);e.pathname+=s,yield e.href}if(n){const e=new URL(i.href);e.pathname+=".html",yield e.href}if(r){const e=r({url:a});for(const t of e)yield t.href}}(s.url,t)){const t=n.get(r);if(t){return{cacheKey:t,integrity:e.getIntegrityForCacheKey(t)}}}},e.strategy)}}function N(e){return k().getCacheKeyForURL(e)}function O(e){return k().matchPrecache(e)}const W={cacheWillUpdate:async({response:e})=>200===e.status||0===e.status?e:null};var j;importScripts("sw-runtime-resources-precache.js"),self.skipWaiting(),self.addEventListener("activate",()=>self.clients.claim());let I=[{'revision':'b9ac6b8549515139303517a8397b8aac','url':'.'},{'revision':null,'url':'VAADIN/build/vaadin-1-4e3ea7c12846e83e31da.cache.js'},{'revision':null,'url':'VAADIN/build/vaadin-2-e8b88d0206c3dc049996.cache.js'},{'revision':null,'url':'VAADIN/build/vaadin-3-3da3169f4d5bf0433388.cache.js'},{'revision':null,'url':'VAADIN/build/vaadin-4-f675eb8429147a516cf9.cache.js'},{'revision':null,'url':'VAADIN/build/vaadin-5-5187ac6632273b0ba8dc.cache.js'},{'revision':null,'url':'VAADIN/build/vaadin-6-4a7cb5adbda2e1af9e29.cache.js'},{'revision':null,'url':'VAADIN/build/vaadin-bundle-8224193b80a97d527f52.cache.js'},{'revision':'a38ca9f0501109549cb659c1fe9ade65','url':'VAADIN/static/line-awesome/dist/line-awesome/fonts/la-brands-400.eot'},{'revision':'9769d0a6a42cf73df12bc63a7a994131','url':'VAADIN/static/line-awesome/dist/line-awesome/fonts/la-brands-400.svg'},{'revision':'bbf83f8bb1039cd860051299d64ebcfd','url':'VAADIN/static/line-awesome/dist/line-awesome/fonts/la-brands-400.ttf'},{'revision':'fb598c9ccecd5fa1c6c769d0be60973b','url':'VAADIN/static/line-awesome/dist/line-awesome/fonts/la-brands-400.woff'},{'revision':'54b0b4e7de85711c3796882b2b19eb00','url':'VAADIN/static/line-awesome/dist/line-awesome/fonts/la-brands-400.woff2'},{'revision':'2746742c09b070f74bd7d555e6b959fa','url':'VAADIN/static/line-awesome/dist/line-awesome/fonts/la-regular-400.eot'},{'revision':'55c57a74937e6de260b67c62522f7ea1','url':'VAADIN/static/line-awesome/dist/line-awesome/fonts/la-regular-400.svg'},{'revision':'87dab6ff12ea107dafe1d52ec19c2ed8','url':'VAADIN/static/line-awesome/dist/line-awesome/fonts/la-regular-400.ttf'},{'revision':'338f6f873b90c8045204f8ac52408166','url':'VAADIN/static/line-awesome/dist/line-awesome/fonts/la-regular-400.woff'},{'revision':'88d9d9416c58bde56378dc4439e3a144','url':'VAADIN/static/line-awesome/dist/line-awesome/fonts/la-regular-400.woff2'},{'revision':'8c65fd3e9b53a609735fd6335fd05841','url':'VAADIN/static/line-awesome/dist/line-awesome/fonts/la-solid-900.eot'},{'revision':'79be4e9fcffc796ec3b2cb9a1f46d39e','url':'VAADIN/static/line-awesome/dist/line-awesome/fonts/la-solid-900.svg'},{'revision':'bb49393b04bbf312a6cd055a051121d3','url':'VAADIN/static/line-awesome/dist/line-awesome/fonts/la-solid-900.ttf'},{'revision':'87292218024ee1cab93406e228a0b7dd','url':'VAADIN/static/line-awesome/dist/line-awesome/fonts/la-solid-900.woff'},{'revision':'36fc297902c9a2e857858baa6ac25f2c','url':'VAADIN/static/line-awesome/dist/line-awesome/fonts/la-solid-900.woff2'}]||[];(null==(j=self.additionalManifestEntries)?void 0:j.length)&&I.push(...self.additionalManifestEntries);const F=new URL(self.registration.scope).pathname;let A=!1;function D(){return{async fetchDidFail(){A=!0},fetchDidSucceed:async({response:e})=>(A=!1,e)}}const H=new class extends b{constructor(e={}){super(e),this._networkTimeoutSeconds=e.networkTimeoutSeconds||0}async _handle(e,t){let s,n=void 0;try{const n=[t.fetch(e)];if(this._networkTimeoutSeconds){const e=p(1e3*this._networkTimeoutSeconds);n.push(e)}if(s=await Promise.race(n),!s)throw new Error("Timed out the network response after "+this._networkTimeoutSeconds+" seconds.")}catch(e){e instanceof Error&&(n=e)}if(!s)throw new r("no-response",{url:e.url,error:n});return s}}({plugins:[D()]});new class extends b{constructor(e={}){super(e),this.plugins.some(e=>"cacheWillUpdate"in e)||this.plugins.unshift(W),this._networkTimeoutSeconds=e.networkTimeoutSeconds||0}async _handle(e,t){const s=[];const n=[];let a;if(this._networkTimeoutSeconds){const{id:r,promise:i}=this._getTimeoutPromise({request:e,logs:s,handler:t});a=r,n.push(i)}const i=this._getNetworkPromise({timeoutId:a,request:e,logs:s,handler:t});n.push(i);const o=await t.waitUntil((async()=>await t.waitUntil(Promise.race(n))||await i)());if(!o)throw new r("no-response",{url:e.url});return o}_getTimeoutPromise({request:e,logs:t,handler:s}){let n;return{promise:new Promise(t=>{n=setTimeout(async()=>{t(await s.cacheMatch(e))},1e3*this._networkTimeoutSeconds)}),id:n}}async _getNetworkPromise({timeoutId:e,request:t,logs:s,handler:n}){let r,a;try{a=await n.fetchAndCachePut(t)}catch(e){e instanceof Error&&(r=e)}return e&&clearTimeout(e),!r&&a||(a=await n.cacheMatch(t)),a}}({plugins:[D()]});var B;S(new class extends T{constructor(e,{allowlist:t=[/./],denylist:s=[]}={}){super(e=>this._match(e),e),this._allowlist=t,this._denylist=s}_match({url:e,request:t}){if(t&&"navigate"!==t.mode)return!1;const s=e.pathname+e.search;for(const e of this._denylist)if(e.test(s))return!1;return!!this._allowlist.some(e=>e.test(s))}}(async e=>{async function t(){const e=await O(".");return e?async function(e){const t=await e.text();return new Response(t.replace(/<base\s+href=[^>]*>/,`<base href="${self.registration.scope}">`),e)}(e):void 0}function s(){return e.url.pathname===F?t():(s=e.url,I.some(e=>N(e.url)===N(""+s))?O(e.request):t());var s}if(!self.navigator.onLine){const e=await s();if(e)return e}try{return await H.handle(e)}catch(e){const t=await s();if(t)return t;throw e}})),function(e){k().precache(e)}(I),function(e){const t=k();S(new E(t,e))}(B),self.addEventListener("message",e=>{var t;"object"==typeof e.data&&"method"in e.data&&"Vaadin.ServiceWorker.isConnectionLost"===e.data.method&&"id"in e.data&&(null==(t=e.source)||t.postMessage({id:e.data.id,result:A},[]))})}]);