<template>
    <!-- <div class="max-w-7xl mx-auto p-12 text-gray-600"> -->
    <div class="flex items-center">
        <select
            v-model="selectedAccountId"
            id="bankAccount"
            class="block w-full px-3 py-0.5 text-base text-gray-700 placeholder-gray-500 border border-gray-300 rounded-md appearance-none focus:outline-none focus:ring-blue-500 focus:border-blue-500">
            <option disabled value="" class="text-gray-800">Select Bank Account</option>
            <option v-for="account in accounts" :key="account.id" :value="account.id">
                {{ account.name }}
            </option>
        </select>
        <input class="w-full" type="file" accept=".pdf,.csv" @change="onFileChange" />
    </div>
</template>

<script>
import axios from 'axios';
import authService from '@/services/authService';

export default {
    data() {
        return {
            accounts: [],
            selectedAccountId: '',
        };
    },
    created() {
        this.fetchBankAccounts();
    },
    methods: {
        fetchBankAccounts() {
            axios
                .get('http://localhost:8080/api/bank-accounts', {
                    headers: { Authorization: 'Bearer ' + authService.getCurrentUser().jwt },
                })
                .then((response) => {
                    this.accounts = response.data;
                })
                .catch((error) => {
                    console.error('Error fetching bank accounts', error);
                });
        },
        onFileChange(event) {
            const file = event.target.files[0];
            if (!file) {
                alert('No file selected');
                return;
            }

            const allowedTypes = ['application/pdf', 'text/csv'];
            if (!allowedTypes.includes(file.type)) {
                alert('Unsupported file type. Please upload a PDF or CSV file.');
                return;
            }

            this.uploadFile(file);
        },
        async uploadFile(file) {
            if (!this.selectedAccountId) {
                alert('Please select a bank account.');
                return;
            }

            const formData = new FormData();
            formData.append('file', file);
            formData.append('accountId', this.selectedAccountId);

            try {
                const response = await axios.post('http://localhost:8080/api/upload', formData, {
                    headers: {
                        'Content-Type': 'multipart/form-data',
                        Authorization: 'Bearer ' + authService.getCurrentUser().jwt,
                    },
                });
                console.log('File uploaded successfully', response.data);
            } catch (error) {
                console.error('Error uploading file', error);
                alert('Error uploading file: ' + error.response?.data || error.message);
            }
        },
    },
};
</script>
