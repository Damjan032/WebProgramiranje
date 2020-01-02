
Vue.use(VueRouter);
const test = { template: '<test></test>' }
const korisnici = { template: '<korisnici></korisnici>' }
const detaljiKorisnika = { template: '<detalji-korisnika></detalji-korisnika>' }

const router = new VueRouter({
    routes: [
    { path: '/',
        component: korisnici},
    { path: '/detaljiKorisnika/:korisnik',
        name:"detaljiKorisnika",
        component: detaljiKorisnika,
        props: true 
    },
    { path: '/test/:id',
        name:"test",
        component: test,
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
        bus.$on('selektovani-korisnik', (korisnik)=>{
            selKorisnik = korisnik;
            console.log(korisnik);
            window.location = "#/detaljiKorisnika/:selKorisnik";

        });

        // bus.$on('kreiran', (message)=>{
        //     bus.$emit('propustSelektovanogKorisnika', this.selektovaniKorisnik);
        // });     
        // bus.$on('selektovaniKorisnik', (korisnik)=>{
        //             this.selektovaniKorisnik = korisnik;
        // });
    },
    methods:{
       
    }
});



