let virtuelnaMasinaPegled = new Vue({
    el:"#virtuelnaMasinaPegled",
    data: {
        virtuelneMasine : null,
        kategorijeBrJezgara : [],
        kategorijeRAM : [],
        kategorijeGPU : [],
        korisnikType : null,
        naziv : "",

    },
    mounted () {
       this.init();


    },
    methods:{
        init:function(){
            axios.get('/virtuelneMasine').then(response => {
                this.virtuelneMasine=response.data;
                this.kategorijeBrJezgara = [];
                this.kategorijeRAM = [];
                this.kategorijeGPU = [];
                for (let vm of this.virtuelneMasine) {
                    this.kategorijeBrJezgara.push(vm.kategorija.brJezgra);
                    this.kategorijeRAM.push(vm.kategorija.RAM);
                    this.kategorijeGPU.push(vm.kategorija.brGPU);
                }
                this.kategorijeGPU = [...new Set(this.kategorijeGPU)];
                this.kategorijeBrJezgara = [...new Set(this.kategorijeBrJezgara)];
                this.kategorijeRAM = [...new Set(this.kategorijeRAM)];
                let minRam =  Math.min.apply(Math,this.kategorijeRAM);
                let maxRam =  Math.max.apply(Math,this.kategorijeRAM);
                $( "#sliderRam" ).slider( "option", "max", maxRam);
                $( "#sliderRam" ).slider( "option", "min", minRam);
                $( "#sliderRam" ).slider( "option", "values",[minRam, maxRam ] );

                let minJezgra =  Math.min.apply(Math,this.kategorijeBrJezgara);
                let maxJezgra =  Math.max.apply(Math,this.kategorijeBrJezgara);
                $( "#sliderJezgra" ).slider( "option", "max", maxJezgra);
                $( "#sliderJezgra" ).slider( "option", "min", minJezgra);
                $( "#sliderJezgra" ).slider( "option", "values",[minJezgra, maxJezgra] );

                let minGPU =  Math.min.apply(Math,this.kategorijeGPU);
                let maxGPU =  Math.max.apply(Math,this.kategorijeGPU);
                $( "#sliderGpu" ).slider( "option", "max", maxGPU);
                $( "#sliderGpu" ).slider( "option", "min", minGPU);
                $( "#sliderGpu" ).slider( "option", "values",[minGPU, maxGPU] );
            });
        },

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
                   axios.get('/korisnik').then(response => {
                       this.virtuelnaMasina=response.data;
                   });
            }).catch(error=> {
               let msg = error.response.data.ErrorMessage;
               new Toast({
                   message: msg,
                   type: 'danger'
               });
           });
        },

        pregledDiskova:function(id){
            window.location.href="/virtuelneMasineDiskovi.html?id="+id;
        },

        izmeni:function(id){
            window.location.href="/virtuelnaMasinaIzmena.html?id="+id;
        },
        filtriraj:function () {
            var valuesRam = $( "#sliderRam" ).slider( "option", "values" );
            var valuesJezgra = $( "#sliderJezgra" ).slider( "option", "values" );
            var valuesGPU = $( "#sliderGpu" ).slider( "option", "values" );
            console.log(this.naziv);
            let pomocniNaziv = this.naziv;
            if(this.naziv.trim() == ""){
                pomocniNaziv=" ";
            }
            axios.get("/virtuelneMasine/filtriraj/"+pomocniNaziv+"/"+valuesRam[0]+"/"+valuesRam[1]+"/"+valuesGPU[0]+"/"+valuesGPU[1]+"/"+valuesJezgra[0]+"/"+valuesJezgra[1]).then(response =>{
                this.virtuelneMasine=response.data;
            }).catch(error=>{
                let msg = error.response.data.ErrorMessage;
                new Toast({
                    message:msg,
                    type: 'danger'
                });
            });
            /*{
                naziv : this.naziv,
                    ramOd : valuesRam[0],
                ramDo : valuesRam[1],
                jezgraOd : valuesJezgra[0],
                jezgraDo : valuesJezgra[1],
                gpuOd : valuesGPU[0],
                gpuDo : valuesGPU[1]
            }*/
        }
    }
});

