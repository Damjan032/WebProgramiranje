jQuery(document).ready(function($) {
    $(".clickable-table-row").click(function() {
        window.location = $(this).data("href");
    });
});

let korisniciapp = new Vue({
    el:"#korisnik",
    data: {
        korisnici : null,
        korisnikType : null,
        items: [
            { message: 'Foo' },
            { message: 'Bar' }
          ]
    },
    created() {
        axios.get('/korisnici').then(response => (
            this.korisnici = response.data));
        axios.get('/korisnik').then(response => (
            this.korisnikType = response.data.uloga)); 
    },
    methods:{
        test: function () {
            for(k of this.korisnici)
            {
                console.log(k);
            }

        }
    }
});



