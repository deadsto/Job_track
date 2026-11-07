/* Spring Boot REST client, kept separate from page rendering. Set window.FUTRACK_API_BASE before app.js to override. */
(() => {
  const base=window.FUTRACK_API_BASE||'http://localhost:8080/api';
  async function request(path,options={}){
    const response=await fetch(`${base}${path}`,{...options,headers:{'Content-Type':'application/json',...(options.headers||{})}});
    if(!response.ok){const message=await response.text();throw new Error(message||`API request failed (${response.status})`)}
    return response.status===204?null:response.json();
  }
  window.FuTrackApi={
    base,
    list:()=>request('/applications'),
    create:application=>{const {id,...body}=application;return request('/applications',{method:'POST',body:JSON.stringify(body)})},
    update:(id,application)=>{const {id:ignored,...body}=application;return request(`/applications/${encodeURIComponent(id)}`,{method:'PUT',body:JSON.stringify(body)})},
    remove:id=>request(`/applications/${encodeURIComponent(id)}`,{method:'DELETE'}),
    health:()=>request('/health')
  };
})();
