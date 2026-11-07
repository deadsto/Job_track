/* Demo persistence boundary. Replace this module's methods with Spring Boot API calls when ready. */
(() => {
  const KEY='greenhouse.applications.v1', USER='greenhouse.user.v1', NOTICES='greenhouse.notifications.v1', PREFS='greenhouse.preferences.v1';
  const today=new Date(); const date=(days)=>{const d=new Date(today);d.setDate(d.getDate()-days);return d.toISOString().slice(0,10)};
  const seed=[
    {id:'a1',company:'Notion',title:'Product Designer',location:'San Francisco, CA',type:'Full-time',mode:'Hybrid',date:date(1),status:'Interview',followUp:date(-2),url:'',recruiter:'',email:'',salary:'',notes:'Hiring manager interview next week.'},
    {id:'a2',company:'Linear',title:'Frontend Engineer',location:'Remote',type:'Full-time',mode:'Remote',date:date(3),status:'Applied',followUp:'',url:'',recruiter:'',email:'',salary:'',notes:''},
    {id:'a3',company:'Figma',title:'UX Researcher',location:'New York, NY',type:'Full-time',mode:'Hybrid',date:date(6),status:'Applied',followUp:'',url:'',recruiter:'',email:'',salary:'',notes:''},
    {id:'a4',company:'Stripe',title:'Product Designer',location:'Remote',type:'Contract',mode:'Remote',date:date(10),status:'Offer',followUp:'',url:'',recruiter:'',email:'',salary:'',notes:'Review offer terms.'},
    {id:'a5',company:'Airbnb',title:'Visual Designer',location:'Seattle, WA',type:'Full-time',mode:'On-site',date:date(15),status:'Rejected',followUp:'',url:'',recruiter:'',email:'',salary:'',notes:''},
    {id:'a6',company:'Webflow',title:'UI Engineer',location:'Remote',type:'Full-time',mode:'Remote',date:date(23),status:'Interview',followUp:date(3),url:'',recruiter:'',email:'',salary:'',notes:'Technical round.'}
  ];
  const read=(key,fallback)=>{try{const x=JSON.parse(localStorage.getItem(key));return x??fallback}catch{return fallback}};
  const write=(key,val)=>localStorage.setItem(key,JSON.stringify(val));
  const ensure=()=>{if(!localStorage.getItem(KEY))write(KEY,seed);if(!localStorage.getItem(NOTICES))write(NOTICES,[{id:'n1',title:'Interview coming up',body:'Your interview with Notion is coming up. Take a moment to prepare.',date:date(0),read:false},{id:'n2',title:'Follow-up reminder',body:'A follow-up with Stripe is due soon.',date:date(0),read:false},{id:'n3',title:'Application sent',body:'Your application to Linear was added to your tracker.',date:date(3),read:true}]);};
  let remoteReady=false;
  const publish=()=>window.dispatchEvent(new CustomEvent('futrack:applications-updated'));
  async function hydrate(){if(!window.FuTrackApi)return;try{const apps=await FuTrackApi.list();write(KEY,apps);remoteReady=true;publish()}catch{/* Keep LocalStorage working when the API is offline. */}}
  async function sync(previous,next){if(!remoteReady||!window.FuTrackApi)return;const before=new Map(previous.map(a=>[a.id,a])),after=new Map(next.map(a=>[a.id,a]));try{for(const [id,old] of before){if(!after.has(id))await FuTrackApi.remove(id);else if(JSON.stringify(old)!==JSON.stringify(after.get(id)))await FuTrackApi.update(id,after.get(id))}for(const [id,value] of after){if(!before.has(id)){const saved=await FuTrackApi.create(value);const current=read(KEY,[]);const i=current.findIndex(a=>a.id===id);if(i>=0)current[i]={...value,id:saved.id};write(KEY,current)}}publish()}catch(error){console.warn('FuTrack API sync unavailable; keeping local data.',error)}}
  window.Store={init:()=>{ensure();hydrate()},all:()=>read(KEY,[]),save:(list)=>{const previous=read(KEY,[]);write(KEY,list);sync(previous,list)},user:()=>read(USER,{name:'Alex Morgan',email:'alex.morgan@email.com'}),setUser:(x)=>write(USER,x),notices:()=>read(NOTICES,[]),saveNotices:(x)=>write(NOTICES,x),prefs:()=>read(PREFS,{theme:'light',reminders:true,updates:true,weekly:true}),savePrefs:(x)=>write(PREFS,x)};
})();
