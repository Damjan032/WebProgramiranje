let virtuelnaMasinaPegled = new Vue({
    el:"#virtuelnaMasinaPegled",
    data: {
        virtuelneMasine : null,
        kategorijeBrJezgara : [],
        kategorijeRAM : [],
        kategorijeGPU : [],
        korisnikType : null,
        filterIme : "",
        filterCPU : "",
        filterRAM : "",
        filterGPU : "",
    },
    mounted () {
        axios.get('/virtuelneMasine').then(response => {
            this.virtuelneMasine = response.data;
            for (let vm of this.virtuelneMasine) {
              this.kategorijeBrJezgara.push(vm.kategorija.brJezgra);
              this.kategorijeRAM.push(vm.kategorija.RAM);
              this.kategorijeGPU.push(vm.kategorija.brGPU);
            }
            this.kategorijeGPU = [...new Set(this.kategorijeGPU)];
            this.kategorijeBrJezgara = [...new Set(this.kategorijeBrJezgara)];
            this.kategorijeRAM = [...new Set(this.kategorijeRAM)];
        });
        axios.get('/korisnik').then(response => {
            this.korisnikType = response.data.uloga;
        });


    },
    methods:{
        pregledAktivnosti:function(id){
             window.location.href="/virtuelneMasineAktinvosti.html?id="+id;
        },
        activnost(vm){
         let promise = axios.put("/virtuelneMasine/activnost/"+vm.id,{});

        },

        filter(event) {
                console.log(event.target.value);
                console.log(this.filterIme);
        },
        obrisi:function(id){
           axios.delete('/virtuelneMasine/'+id).then(response => {
                      window.location.reload();
            });
        },

        pregledDiskova:function(id){
            window.location.href="/virtuelneMasineDiskovi.html?id="+id;
        },

        izmeni:function(id){
            window.location.href="/virtuelnaMasinaIzmena.html?id="+id;
        }
    }
});