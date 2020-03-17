// Vue.use(VueRouter);
// const diskovi = { template: '<diskovi></diskovi>' }
// const detaljiDiska = { template: '<detalji-diska></detalji-diska>' }
// const dodajDIsk = { template: '<dodaj-disk></dodaj-disk>' }

// const router = new VueRouter({
//     routes: [
//         { path: '/',
//             component: diskovi
//         },
//         { path: '/dodajDisk',
//             component: dodajDIsk
//         },  
//         { path: '/detaljiDiska/:disk/:tipKorisnika',
//             name:"detaljiDiska",
//             component: detaljiDiska,
//             props: true 
//         }
//     ]
// });

Vue.component("diskovi",{
    template:
`
<div id="diskapp">
    <v-container color="primary">
        <v-layout align-center>
            <v-flex text-xs-center>
                <h1 class="display-2">Diskovi</h1>
            </v-flex>
        </v-layout>
        <v-divider class="my-3">
        </v-divider>
    </v-container>  
    <v-container>  
        <transition name="fade">
            <router-view></router-view>
        </transition>
    </v-container>
</div>
`
});