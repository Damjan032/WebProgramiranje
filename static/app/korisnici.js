
Vue.use(VueRouter);
const korisnici = { template: '<korisnici></korisnici>' }
const detaljiKorisnika = { template: '<detalji-korisnika></detalji-korisnika>' }

const router = new VueRouter({
    routes: [
        { path: '/',
            component: korisnici
        },
        { path: '/detaljiKorisnika/:korisnik',
            name:"detaljiKorisnika",
            component: detaljiKorisnika,
            props: true 
        }
    ]
});

let korisniciapp = new Vue({
    router,
    el:"#kor",
    data: {
        korisnici : null,
        selKorisnik:null,
        korisnikType : null
    },
    mounted() {
    },
    methods:{
       
    }
});



