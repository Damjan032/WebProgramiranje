jQuery(document).ready(function($) {
    $(".clickable-table-row").click(function() {
        window.location = $(this).data("href");
    });
});

let korisniciapp = new Vue({
    el:"#korisnici",
    data: {
        korisnici : null,
        korisnikType : null
    },
    mounted () {
        axios.get('/korisnici').then(response => {
            this.korisnici = response.data;
        }); 
        axios.get('/tipKorisnika').then(response => {
            this.korisnikType = response.data;
        }); 
    },
    methods:{
        
    }
});



