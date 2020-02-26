// Vue.use(VueRouter);
// const kat = { template: '<kat></kat>' }
// const detaljiKat = { template: '<detalji-kat></detalji-kat>' }
// const dodajKat = { template: '<dodaj-kat></dodaj-kat>' }

// const router = new VueRouter({
//     routes: [
//         { path: '/',
//             component: kat
//         },
//         { path: '/dodajKat',
//             component: dodajKat
//         },  
//         { path: '/detaljiKat/:kat/:tipKorisnika',
//             name:"detaljiKat",
//             component: detaljiKat,
//             props: true 
//         }
//     ]
// });

Vue.component("kat",{
    template:
`
<div id="katapp">
    <div class="jumbotron"><h1>Kategorije virtuelnih mašina</h1></div>  
    <div class="container">
        <router-view></router-view>
    </div>    
</div>
`
});