// Vue.use(VueRouter);
// const kat = { template: '<kat></kat>' }
// const detaljiKat = { template: '<detalji-kat></detalji-kat>' }
// const dodajKat = { template: '<dodaj-kat></dodaj-kat>' }

// const router = new VueRouter({
//     routes: [
//         { path: '/',
//             component: kat
//         },
//         { path: '/dodajKat',
//             component: dodajKat
//         },  
//         { path: '/detaljiKat/:kat/:tipKorisnika',
//             name:"detaljiKat",
//             component: detaljiKat,
//             props: true 
//         }
//     ]
// });

Vue.component("kat",{
    template:
`
<div id="katapp">
    <v-container color="primary">
        <v-layout align-center>
            <v-flex text-xs-center>
                <h1 class="display-2">Kategorije virtuelnih mašina</h1>
            </v-flex>
        </v-layout>
        <v-divider class="my-3"></v-divider>
    </v-container>
    <div class="container">
        <transition name="fade">
            <router-view></router-view>
        </transition>
    </div>    
</div>
`
});