// Vue.use(VueRouter);
// const diskovi = { template: '<diskovi></diskovi>' }
// const detaljiDiska = { template: '<detalji-diska></detalji-diska>' }
// const dodajDIsk = { template: '<dodaj-disk></dodaj-disk>' }

// const router = new VueRouter({
//     routes: [
//         { path: '/',
//             component: diskovi
//         },
//         { path: '/dodajDisk',
//             component: dodajDIsk
//         },  
//         { path: '/detaljiDiska/:disk/:tipKorisnika',
//             name:"detaljiDiska",
//             component: detaljiDiska,
//             props: true 
//         }
//     ]
// });

Vue.component("diskovi",{
    template:
`
<div id="diskapp">
    <div class="jumbotron"><h1>Diskovi</h1></div>  
    <div class="container">
        <router-view></router-view>
    </div>    
</div>
`
});