jQuery(document).ready(function($) {
    $(".clickable-table-row").click(function() {
        window.location = $(this).data("href");
    });
});


const korisnici = { template: '<korisnici></korisnici>' }
const detaljiKorisnika = { template: '<detalji-korisnika></detalji-korisnika>' }

const router = new VueRouter({
	  mode: 'hash',
	  routes: [
	    { path: '/', component: korisnici},
	    { path: '/detaljiKorisnika', component: detaljiKorisnika }
	  ]
});

let korisniciapp = new Vue({
    router,
    el:"#kor",
    data: {
        korisnici : null,
        selektovaniKorisnik:null,
        korisnikType : null
    },
    mounted() {
        bus.$on('selektovaniKorisnik', (korisnik)=>{
                    this.selektovaniKorisnik = korisnik;
        });
    },
    methods:{
       
    }
});



