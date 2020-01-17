jQuery(document).ready(function($) {
    $(".clickable-table-row").click(function() {
        window.location = $(this).data("href");
    });
});

let orgapp = new Vue({
    el:"#orgs",
    data: {
        organizacije : null,
        korisnikType : null
    },
    mounted () {
        axios.get('/organizacije').then(response => {
            this.organizacije = response.data;
        }); 
        axios.get('/korisnik').then(response => {
            this.korisnikType = response.data.uloga;
        }); 
    },
    methods:{
        
    }
});



