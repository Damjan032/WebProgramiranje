
// Vue.use(VueRouter);
// const korisnici = { template: '<korisnici></korisnici>' };
// const detaljiKorisnika = { template: '<detalji-korisnika></detalji-korisnika>' };
// const dodajKorisnika = {template:"<dodaj-korisnika></dodaj-korisnika>"};
// const router = new VueRouter({
//     routes: [
//         { path: '/',
//             component: korisnici
//         },
//         { path: '/detaljiKorisnika/:korisnik',
//             name:"detaljiKorisnika",
//             component: detaljiKorisnika,
//             props: true 
//         },
//         { path: '/dodajKorisnika',
//             component: dodajKorisnika,
//         }
//     ]
// });

Vue.component("korisnici",{
    template:
`
<div>
    <div class="jumbotron"><h1>Korisnici</h1></div>

    <div class="container">
        <router-view :selektovani-korisnik="selKorisnik"></router-view>
    </div>

</div>
    
`
});



