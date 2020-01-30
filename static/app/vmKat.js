Vue.use(VueRouter);
const kat = { template: '<kat></kat>' }
const detaljiKat = { template: '<detalji-kat></detalji-kat>' }
const dodajKat = { template: '<dodaj-kat></dodaj-kat>' }

const router = new VueRouter({
    routes: [
        { path: '/',
            component: kat
        },
        { path: '/dodajKat',
            component: dodajKat
        },  
        { path: '/detaljiKat/:kat/:tipKorisnika',
            name:"detaljiKat",
            component: detaljiKat,
            props: true 
        }
    ]
});

let diskapp = new Vue({
    router,
    el:"#katapp"
});